package com.northwind.controller.customer;

import com.northwind.context.AuthenticatedUserContext;
import com.northwind.context.CustomerContext;
import com.northwind.exceptions.CartServiceException;
import com.northwind.model.entity.Cart;
import com.northwind.model.entity.CartItem;
import com.northwind.model.entity.Customer;
import com.northwind.model.entity.Product;
import com.northwind.service.*;
import com.northwind.util.constants.messages.CartMessages;
import com.northwind.util.constants.messages.GrowlTitles;
import com.northwind.util.constants.messages.OrderMessages;
import com.northwind.util.jsf.GrowlUtil;

import static com.northwind.util.validation.CartValidatorUtil.*;
import static com.northwind.util.exception.BeanExceptionUtil.handle;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Managed bean that handles the shopping cart logic for the authenticated customer.
 * Responsible for adding, removing, updating products and processing the final purchase.
 * <p>
 * The bean initializes automatically with the customer's cart if a user is authenticated.
 */
@Getter
@Setter
@Named
@SessionScoped
public class CartBean implements Serializable {

    private Cart cart;
    private List<CartItem> items;
    private Map<Integer, Integer> selectedQuantities;
    private Integer productIdToRemove;
    private Set<Integer> productIdsInCart;

    @Inject
    private iCartService cartService;
    @Inject
    private iProductService productService;
    @Inject
    private iCustomerService customerService;
    @Inject
    private iPurchaseService purchaseService;

    @Inject
    private AuthenticatedUserContext authUserContext;
    @Inject
    private CustomerContext customerContext;

    @Inject
    private Logger logger;

    @PostConstruct
    public void init() {
        selectedQuantities = new HashMap<>();
        productIdsInCart = new HashSet<>();
        authUserContext.ifUserIdPresent(this::loadCustomerCart);
    }

    /**
     * Adds a product to the current cart, after validating available stock.
     * Also updates the product's stock locally and shows success or error messages.
     *
     * @param product  the product to add
     * @param quantity the quantity to add
     */
    public void addProductToCart(Product product, short quantity) {
        try {
            short finalQuantity = normalizeQuantity(quantity);

            if (!validateStockAndWarn(product, finalQuantity)) {
                return;
            }

            cartService.addProductToCart(cart.getId(), product.getId(), finalQuantity);
            productIdsInCart.add(product.getId());

            product.setStock((short) (product.getStock() - finalQuantity));
            selectedQuantities.remove(product.getId());

            GrowlUtil.success(GrowlTitles.SUCCESS_OPERATION_COMPLETED,
                    String.format(CartMessages.SUCCESS_ADD_TO_CART, product.getName(), finalQuantity));
        } catch (Exception e) {
            handle(logger, "adding product to cart", e, CartMessages.ERROR_ADD_TO_CART, Level.SEVERE);
        }
    }

    public void prepareProductRemoval(Integer productId) {
        this.productIdToRemove = productId;
    }

    public void removeProductFromCart() {
        if (productIdToRemove != null) {
            removeProductFromCart(productIdToRemove);
            productIdToRemove = null;
        }
    }

    public void clearCart(boolean showMessage) {
        try {
            if (isCartEmpty()) {
                if (showMessage) {
                    GrowlUtil.warning(GrowlTitles.INFO_MESSAGE, CartMessages.CART_EMPTY_ADVICE);
                }
                return;
            }

            cartService.clearCart(cart.getId());
            refreshCart();

            if (showMessage) {
                GrowlUtil.success(GrowlTitles.SUCCESS_OPERATION_COMPLETED, CartMessages.SUCCESS_CLEAR_CART);
            }
        } catch (Exception e) {
            handle(logger, "clearing cart", e, CartMessages.ERROR_CLEAR_CART, Level.SEVERE);
        }
    }

    public void clearCart() {
        clearCart(true);
    }

    public void updateQuantity(CartItem cartItem) {
        try {
            Product product = cartItem.getProduct();
            short finalQuantity = normalizeQuantity(cartItem.getQuantity());

            if (!validateStockAndWarn(product, finalQuantity)) {
                cartItem.setQuantity((short) Math.min(product.getStock(), cartItem.getQuantity()));
                return;
            }

            cartService.saveCart(cart);
        } catch (Exception e) {
            handle(logger, "updating cart quantity", e, CartMessages.ERROR_UPDATE_CART, Level.SEVERE);
        }
    }

    /**
     * Finalizes the purchase of all items in the cart for the authenticated customer.
     * If the cart is empty, shows a warning. Otherwise, processes the order and clears the cart.
     */
    public void purchaseCart() {
        try {
            Customer customer = customerContext.requireCustomer();

            if (isCartEmpty()) {
                GrowlUtil.warning(GrowlTitles.ERROR_OPERATION_FAILED, CartMessages.CART_EMPTY_ADVICE);
                return;
            }

            purchaseService.purchaseCartForUser(customer);
            refreshCart();

            GrowlUtil.success(GrowlTitles.SUCCESS_OPERATION_COMPLETED, CartMessages.SUCCESS_PURCHASE);
        } catch (Exception e) {
            handle(logger, "purchase cart", e, OrderMessages.ORDER_PROCESSING_ERROR, Level.SEVERE);
        }
    }

    public BigDecimal getTotal() {
        return cart == null || cart.getItems() == null
                ? BigDecimal.ZERO
                : cart.getItems().stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getCartItemCount() {
        return (cart != null) ? cartService.getCartItemCount(cart.getId()) : 0;
    }

    public boolean isCartEmpty() {
        return cart == null || cart.getItems().isEmpty();
    }

    public boolean isProductAlreadyInCart(Integer productId) {
        return productIdsInCart.contains(productId);
    }

    /**
     * Loads and initializes the cart for the customer associated with the given user ID.
     * If no customer is found, logs and shows a warning.
     *
     * @param userId the authenticated user's ID
     */
    public void loadCustomerCart(Integer userId) {
        customerService.getByUserId(userId)
                .ifPresentOrElse(this::initializeCartForCustomer,
                        () -> handle(logger,
                                "no customer found for user ID: " + userId,
                                new IllegalStateException("Customer not found"),
                                CartMessages.ERROR_NO_CUSTOMER_FOUND,
                                Level.WARNING));
    }

    public void reloadCart() {
        authUserContext.ifUserIdPresent(userId -> {
            customerService.getByUserId(userId).ifPresent(this::initializeCartForCustomer);
        });
    }

    /**
     * Initializes the cart and items for the given customer.
     * Ensures the internal tracking of product IDs is also loaded.
     *
     * @param customer the customer whose cart should be loaded or created
     */
    private void initializeCartForCustomer(Customer customer) {
        cart = cartService.getOrCreateCartForCustomer(customer);
        items = cart.getItems();
        loadProductIdsInCart();
    }

    /**
     * Reloads the cart and its items from the database to ensure the view is up to date.
     * Also updates the internal product ID tracking set.
     */
    private void refreshCart() {
        try {
            if (cart != null) {
                cart = cartService.getCartById(cart.getId())
                        .orElseThrow(() -> new CartServiceException("Cart not found with ID: " + cart.getId()));
                items = cart.getItems();
                productIdToRemove = null;
                loadProductIdsInCart();
            }
        } catch (Exception e) {
            handle(logger, "refreshing cart", e, CartMessages.ERROR_LOADING_CART, Level.SEVERE);
        }
    }

    private void removeProductFromCart(Integer productId) {
        try {
            Product product = productService.requireProductById(productId);
            cartService.removeProductFromCart(cart.getId(), productId);
            refreshCart();
            GrowlUtil.success(GrowlTitles.SUCCESS_OPERATION_COMPLETED,
                    String.format(CartMessages.SUCCESS_REMOVE_FROM_CART, product.getName()));
        } catch (Exception e) {
            handle(logger, "removing product from cart", e, CartMessages.ERROR_REMOVE_FROM_CART, Level.SEVERE);
        }
    }

    private void loadProductIdsInCart() {
        productIdsInCart.clear();
        if (cart != null && cart.getItems() != null) {
            for (CartItem item : cart.getItems()) {
                productIdsInCart.add(item.getProduct().getId());
            }
        }
    }

    private boolean validateStockAndWarn(Product product, short quantity) {
        if (!isStockAvailable(product, quantity)) {
            GrowlUtil.warning(
                    GrowlTitles.ERROR_OPERATION_FAILED,
                    String.format(CartMessages.ERROR_INSUFFICIENT_STOCK, product.getName())
            );
            return false;
        }
        return true;
    }
}
