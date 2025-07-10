package com.northwind.exceptions;

import jakarta.ejb.ApplicationException;

import java.io.Serializable;

/**
 * Base exception for authentication service failures.
 * <p>
 * {@code @ApplicationException(rollback = false)} indicates that:
 * - The exception won't trigger transaction rollback
 * - Won't be wrapped in EJBException by the container
 */
@ApplicationException(rollback = false)
public class AuthServiceException extends RuntimeException implements Serializable {

    public AuthServiceException(String message) {
        super(message);
    }

    public AuthServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
