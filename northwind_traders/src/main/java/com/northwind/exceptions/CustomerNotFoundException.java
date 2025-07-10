package com.northwind.exceptions;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException() {
        super("Customer entity not found");
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}