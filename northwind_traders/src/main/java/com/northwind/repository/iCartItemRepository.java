package com.northwind.repository;

import com.northwind.model.entity.CartItem;

import java.util.List;

/**
 * Repository interface for performing database operations related to {@link CartItem}.
 * <p>
 * Extends {@code iCrudRepository} to provide standard CRUD functionality.
 * </p>
 */
public interface iCartItemRepository extends iCrudRepository<CartItem, Integer> {

    List<CartItem> findByCartId(Integer cartId);

    /**
     * Retrieves a cart item by cart ID and product ID.
     * Useful to check if a product is already in the cart.
     *
     * @param cartId    the ID of the cart
     * @param productId the ID of the product
     * @return the matching cart item, or {@code null} if not found
     */
    CartItem findByCartIdAndProductId(Integer cartId, Integer productId);

    /**
     * Retrieves all cart items for a customer, including their associated products.
     * Typically used to show cart content in the UI.
     *
     * @param customerId the ID of the customer
     * @return a list of cart items with product data
     */
    List<CartItem> findByCustomerIdWithProducts(Integer customerId);
}
