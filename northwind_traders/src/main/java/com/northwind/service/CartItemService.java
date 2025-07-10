package com.northwind.service;

import com.northwind.exceptions.CartItemServiceException;
import com.northwind.model.entity.Cart;
import com.northwind.model.entity.CartItem;
import com.northwind.model.entity.Product;
import com.northwind.repository.iCartItemRepository;
import com.northwind.service.factory.CartItemFactory;
import com.northwind.util.exception.ServiceExceptionUtil;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class CartItemService implements iCartItemService {

    @Inject
    private iCartItemRepository cartItemRepo;
    @Inject
    private CartItemFactory cartItemFactory;
    @Inject
    private Logger logger;

    @Override
    public Optional<CartItem> getCartItemById(Integer id) {
        return Optional.ofNullable(cartItemRepo.findById(id));
    }

    @Override
    public List<CartItem> getAllCartItems() {
        return cartItemRepo.findAll();
    }

    @Override
    public void saveCartItem(CartItem cartItem) {
        try {
            cartItemRepo.save(cartItem);
            logger.log(Level.INFO, "Cart item saved with ID: {0}", cartItem.getId());
        } catch (RuntimeException e) {
            handleException("saving cart item with ID: " + cartItem.getId(), e, Level.SEVERE);
        }
    }

    @Override
    public void deleteCartItem(Integer id) {
        try {
            CartItem cartItem = findCartItemByIdOrThrow(id);
            cartItemRepo.delete(id);
            logger.log(Level.INFO, "Cart item deleted with ID: {0}", id);
        } catch (RuntimeException e) {
            handleException("deleting cart item with ID: " + id, e, Level.SEVERE);
        }
    }

    @Override
    public Optional<CartItem> getByCartIdAndProductId(Integer cartId, Integer productId) {
        return Optional.ofNullable(cartItemRepo.findByCartIdAndProductId(cartId, productId));
    }

    @Override
    public List<CartItem> getByCartId(Integer cartId) {
        return cartItemRepo.findByCartId(cartId);
    }

    @Override
    public List<CartItem> getByCustomerIdWithProducts(Integer customerId) {
        return cartItemRepo.findByCustomerIdWithProducts(customerId);
    }

    private CartItem findCartItemByIdOrThrow(Integer id) {
        return Optional.ofNullable(cartItemRepo.findById(id))
                .orElseThrow(() -> new CartItemServiceException("Cart item not found with ID: " + id));
    }

    /**
     * Removes a product from the cart's item list.
     * NOTE: Does not persist the cart. Caller must save the cart after this operation.
     */
    @Override
    public boolean removeProductFromCart(Cart cart, Integer productId) {
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new CartItemServiceException("Product with ID " + productId + " not found in cart."));

        return cart.getItems().remove(cartItem);
    }

    /**
     * Clears all items from the cart.
     * NOTE: Does not persist the cart. Caller must save the cart after this operation.
     */
    @Override
    public void clearCartItems(Cart cart) {
        cart.getItems().clear();
    }

    @Override
    public CartItem getOrCreateCartItem(Cart cart, Product product, short quantity) {
        return findCartItemInCart(cart, product)
                .map(item -> {
                    item.updateQuantity(quantity);
                    return item;
                })
                .orElseGet(() -> createAndSaveCartItem(cart, product, quantity));
    }

    private Optional<CartItem> findCartItemInCart(Cart cart, Product product) {
        return cart.getItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst();
    }

    private CartItem createAndSaveCartItem(Cart cart, Product product, short quantity) {
        CartItem newCartItem = cartItemFactory.createCartItem(cart, product, quantity);
        cart.getItems().add(newCartItem);
        saveCartItem(newCartItem);
        return newCartItem;
    }

    private void handleException(String action, Exception e, Level level) {
        ServiceExceptionUtil.handle(logger, action, e, level, CartItemServiceException.class, null
        );
    }

}
