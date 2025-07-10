package com.northwind.exceptions;

public class CartItemServiceException extends RuntimeException {

    public CartItemServiceException(String message) {
        super(message);
    }

    public CartItemServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
