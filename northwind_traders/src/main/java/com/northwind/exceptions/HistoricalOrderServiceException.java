package com.northwind.exceptions;

public class HistoricalOrderServiceException extends RuntimeException {

    public HistoricalOrderServiceException(String message) {
        super(message);
    }

    public HistoricalOrderServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
