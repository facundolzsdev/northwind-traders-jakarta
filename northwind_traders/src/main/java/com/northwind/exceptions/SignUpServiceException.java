package com.northwind.exceptions;

public class SignUpServiceException extends RuntimeException {

    public SignUpServiceException(String message) {
        super(message);
    }

    public SignUpServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
