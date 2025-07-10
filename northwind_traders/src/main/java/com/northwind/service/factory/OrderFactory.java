package com.northwind.service.factory;

import com.northwind.model.entity.Customer;
import com.northwind.model.entity.Order;
import com.northwind.model.enums.OrderStatus;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;

/**
 * Factory for creating Order entities with default initialization.
 * Sets initial status and order date for new customer orders.
 */
@ApplicationScoped
public class OrderFactory {

    /**
     * Creates a new Order for the given customer.
     * Initializes the order with current timestamp and default status 'PENDIENTE'.
     *
     * @param customer the customer placing the order.
     * @return a new Order instance ready for persistence.
     */
    public Order createOrder(Customer customer) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDIENTE);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
}