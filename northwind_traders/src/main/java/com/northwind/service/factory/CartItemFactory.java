package com.northwind.service.factory;

import com.northwind.model.entity.Cart;
import com.northwind.model.entity.CartItem;
import com.northwind.model.entity.Product;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Factory for creating CartItem entities from a product and its quantity.
 * Sets price based on the current product price at the time of addition.
 */
@ApplicationScoped
public class CartItemFactory {

    /**
     * Creates a CartItem with the given cart, product, and quantity.
     * Uses the product's current price for the item.
     *
     * @param cart     the cart to which the item belongs.
     * @param product  the product being added.
     * @param quantity the quantity of the product.
     * @return a new CartItem instance.
     */
    public CartItem createCartItem(Cart cart, Product product, short quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setPrice(product.getPrice());
        cartItem.setQuantity(quantity);
        return cartItem;
    }
}