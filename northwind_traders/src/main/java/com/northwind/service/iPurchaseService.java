package com.northwind.service;

import com.northwind.exceptions.PurchaseServiceException;
import com.northwind.model.entity.Customer;
import com.northwind.model.entity.Order;

/**
 * Service interface for handling the purchase process of a customer's cart.
 * Defines the contract for converting the current cart of a user into a finalized order.
 */
public interface iPurchaseService {

    /**
     * Processes the purchase of the cart associated with the given user ID.
     * This includes validating the cart contents, generating an order with its details,
     * saving the order, and clearing the cart afterwards.
     *
     * @param customer The ID of the user who is purchasing their cart
     * @return The newly created Order instance
     * @throws PurchaseServiceException if any error occurs during the purchase process
     */
    Order purchaseCartForUser(Customer customer);
}