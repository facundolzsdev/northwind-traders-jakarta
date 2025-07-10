package com.northwind.service;

import com.northwind.exceptions.CartServiceException;
import com.northwind.exceptions.ProductNotFoundException;
import com.northwind.model.entity.Cart;
import com.northwind.model.entity.CartItem;
import com.northwind.model.entity.Customer;
import com.northwind.model.entity.Product;
import com.northwind.model.support.Audit;
import com.northwind.repository.iCartRepository;
import com.northwind.util.exception.ServiceExceptionUtil;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class CartServiceImpl implements iCartService {

    @Inject
    private iCartRepository cartRepo;
    @Inject
    private iProductService productService;
    @Inject
    private iUserService userService;
    @Inject
    private iCustomerService customerService;
    @Inject
    private iCartItemService cartItemService;
    @Inject
    private Logger logger;

    @Override
    public Optional<Cart> getCartById(Integer id) {
        return Optional.ofNullable(cartRepo.findById(id));
    }

    @Override
    public List<Cart> getAllCarts() {
        return cartRepo.findAll();
    }

    @Override
    public void saveCart(Cart cart) {
        try {
            cartRepo.save(cart);
        } catch (Exception e) {
            handleException("saving cart", e, Level.SEVERE);
        }
    }

    @Override
    public void deleteCart(Integer id) {
        try {
            Cart cart = findCartByIdOrThrow(id, "Failed to delete cart.");
            cartRepo.delete(id);
            logger.log(Level.INFO, "Cart deleted with ID: {0}", id);
        } catch (CartServiceException e) {
            throw e;
        } catch (Exception e) {
            handleException("deleting cart with ID: " + id, e, Level.SEVERE);
        }
    }

    @Override
    public Cart getOrCreateCartForCustomer(Customer customer) {
        return Optional.ofNullable(cartRepo.findByCustomerIdWithItems(customer.getId()))
                .orElseGet(() -> createCartForCustomer(customer));
    }

    @Override
    public void addProductToCart(Integer cartId, Integer productId, short quantity) {
        try {
            Cart cart = findCartByIdOrThrow(cartId, "Cannot add product, cart not found.");
            Product product = findProductByIdOrThrow(productId);
            CartItem cartItem = cartItemService.getOrCreateCartItem(cart, product, quantity);
            saveCart(cart);
        } catch (ProductNotFoundException | CartServiceException e) {
            throw e;
        } catch (Exception e) {
            handleException("adding product to cart", e, Level.SEVERE);
        }
    }

    @Override
    public void clearCart(Integer cartId) {
        Cart cart = findCartByIdOrThrow(cartId, "Failed to clear cart.");
        cartItemService.clearCartItems(cart);
        saveCart(cart);
    }

    @Override
    public void removeProductFromCart(Integer cartId, Integer productId) {
        try {
            Cart cart = findCartByIdOrThrow(cartId, "Attempted to remove product from non-existent cart.");
            cartItemService.removeProductFromCart(cart, productId);
            saveCart(cart);
        } catch (ProductNotFoundException | CartServiceException e) {
            throw e;
        } catch (Exception e) {
            handleException("removing product from cart", e, Level.SEVERE);
        }
    }

    @Override
    public int getCartItemCount(Integer cartId) {
        return cartRepo.getCartItemCount(cartId);
    }

    @Override
    public Cart getByCustomerIdWithItems(Integer customerId) {
        return cartRepo.findByCustomerIdWithItems(customerId);
    }

    @Override
    public void deleteCartByCustomerId(Integer customerId) {
        try {
            Cart cart = getByCustomerIdWithItems(customerId);
            if (cart != null) {
                cart.getItems().clear();
                cartRepo.delete(cart.getId());
                logger.log(Level.INFO, "Cart and items deleted for customer ID: {0}", customerId);
            } else {
                logger.log(Level.INFO, "No cart found for customer ID: {0}", customerId);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting cart for customer ID: " + customerId, e);
            throw new CartServiceException("Failed to delete cart for customer.", e);
        }
    }

    private Cart createCartForCustomer(Customer customer) {
        Cart newCart = new Cart();
        newCart.setCustomer(customer);
        newCart.setAudit(Audit.createDefaultAudit());
        saveCart(newCart);
        customer.setCart(newCart);
        return newCart;
    }

    private Product findProductByIdOrThrow(Integer productId) {
        return productService.getProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
    }

    private Cart findCartByIdOrThrow(Integer cartId, String errorMessage) {
        return getCartById(cartId)
                .orElseThrow(() -> new CartServiceException(errorMessage + " Cart ID: " + cartId));
    }

    private void handleException(String action, Exception e, Level level) {
        ServiceExceptionUtil.handle(logger, action, e, level, CartServiceException.class, null
        );
    }
}
