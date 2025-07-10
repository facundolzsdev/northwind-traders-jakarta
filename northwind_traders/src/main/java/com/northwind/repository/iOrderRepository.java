package com.northwind.repository;

import com.northwind.model.entity.Order;

import java.util.List;

/**
 * Repository interface for performing database operations related to {@link Order}.
 * <p>
 * Extends {@code iCrudRepository} to provide standard CRUD functionality.
 * </p>
 */
public interface iOrderRepository extends iCrudRepository<Order, Integer> {

    /**
     * Retrieves an order by its ID and the associated customer's ID, including its details.
     * If no matching order is found, returns {@code null}.
     *
     * @param customerId the ID of the customer who owns the order.
     * @param orderId    the ID of the order to retrieve.
     * @return the {@link Order} instance if it exists and belongs to the specified customer;
     * {@code null} if not found or the order does not belong to the customer.
     */
    Order findByCustomerIdAndOrderId(Integer customerId, Integer orderId);

    /**
     * Retrieves an order by its ID, including its associated details such as order items.
     *
     * @param id the ID of the order
     * @return the order with its details, or {@code null} if not found
     */
    Order findByIdWithDetails(Integer id);

    /**
     * Retrieves all orders placed by a specific customer, including their order details.
     *
     * @param customerId the ID of the customer
     * @return a list of orders for the specified customer
     */
    List<Order> findByCustomerId(Integer customerId);

    /**
     * Retrieves all processed orders for a specific customer.
     * Each order includes its associated order details and product information.
     *
     * @param customerId the ID of the customer
     * @return a list of completed orders for the specified customer
     */
    List<Order> findProcessedOrdersByCustomerId(Integer customerId);

    /**
     * Retrieves all orders with status 'Pending'.
     * Each order includes its associated customer, order details, and product information.
     *
     * @return a list of pending orders with full detail
     */
    List<Order> findAllPendingWithDetails();

}
