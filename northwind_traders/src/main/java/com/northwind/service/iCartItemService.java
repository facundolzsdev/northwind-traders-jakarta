package com.northwind.service;

import com.northwind.model.entity.Cart;
import com.northwind.model.entity.CartItem;
import com.northwind.model.entity.Product;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for handling cart item-related business logic.
 */
public interface iCartItemService {

    Optional<CartItem> getCartItemById(Integer id);

    List<CartItem> getAllCartItems();

    void saveCartItem(CartItem cartItem);

    void deleteCartItem(Integer id);

    List<CartItem> getByCartId(Integer cartId);

    Optional<CartItem> getByCartIdAndProductId(Integer cartId, Integer productId);

    List<CartItem> getByCustomerIdWithProducts(Integer customerId);

    // ** SERVICE-LAYER SPECIFIC METHODS ** //

    /**
     * Finds an existing cart item by product in the given cart, or creates a new one with the specified quantity.
     *
     * @param cart     the cart to search within
     * @param product  the product to find or add
     * @param quantity the quantity to set if a new cart item is created
     * @return the existing or newly created CartItem
     */
    CartItem getOrCreateCartItem(Cart cart, Product product, short quantity);

    boolean removeProductFromCart(Cart cart, Integer productId);

    void clearCartItems(Cart cart);
}
