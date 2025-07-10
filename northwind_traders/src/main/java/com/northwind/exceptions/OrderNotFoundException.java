package com.northwind.exceptions;

public class OrderNotFoundException extends OrderServiceException {

    public OrderNotFoundException(Integer orderId) {
        super("Order with ID " + orderId + " was not found.");
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}