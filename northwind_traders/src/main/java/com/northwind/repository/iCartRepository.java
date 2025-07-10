package com.northwind.repository;

import com.northwind.model.entity.Cart;

/**
 * Repository interface for performing database operations related to {@link Cart}.
 * <p>
 * Extends {@code iCrudRepository} to provide standard CRUD functionality.
 * </p>
 */
public interface iCartRepository extends iCrudRepository<Cart, Integer> {

    /**
     * Returns the total number of items in the specified cart.
     *
     * @param cartId the ID of the cart
     * @return the number of cart items
     */
    int getCartItemCount(Integer cartId);

    /**
     * Retrieves the cart associated with the given customer ID,
     * including its cart items.
     *
     * @param customerId the ID of the customer
     * @return the customer's cart with its items, or {@code null} if not found
     */
    Cart findByCustomerIdWithItems(Integer customerId);

}
