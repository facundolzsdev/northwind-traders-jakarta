package com.northwind.exceptions;

public class EmployeeNotFoundException extends RuntimeException{

    public EmployeeNotFoundException() {
        super("Employee entity not found");
    }

    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
