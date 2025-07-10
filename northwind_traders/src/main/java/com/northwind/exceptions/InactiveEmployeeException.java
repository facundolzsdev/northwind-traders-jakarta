package com.northwind.exceptions;

public class InactiveEmployeeException extends AuthServiceException {
    public InactiveEmployeeException() {
        super("The employee is not authorized to login.");
    }
}