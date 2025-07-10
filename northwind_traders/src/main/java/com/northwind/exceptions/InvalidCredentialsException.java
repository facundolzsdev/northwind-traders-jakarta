package com.northwind.exceptions;

import java.io.Serializable;

public class InvalidCredentialsException extends RuntimeException implements Serializable {
    public InvalidCredentialsException() {
        super("Invalid credentials provided");
    }
}