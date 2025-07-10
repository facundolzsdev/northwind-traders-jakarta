package com.northwind.util.exception;

import lombok.experimental.UtilityClass;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for consistent exception handling in the service layer.
 * Logs the error and throws the appropriate exception based on its type.
 */
@UtilityClass
public class ServiceExceptionUtil {

    /**
     * Handles an exception thrown during a service action.
     * Logs the error and decides whether to rethrow the original or wrap it.
     *
     * @param logger                 Logger to use for logging the error.
     * @param action                 Description of the action being performed (e.g., "saving product").
     * @param e                      The caught exception.
     * @param level                  Log level (e.g., Level.SEVERE).
     * @param serviceExceptionClass  Type of service exception to throw.
     * @param notFoundExceptionClass (Optional) Specific "not found" exception to rethrow if applicable.
     */
    public static void handle(
            Logger logger,
            String action,
            Exception e,
            Level level,
            Class<? extends RuntimeException> serviceExceptionClass,
            Class<? extends RuntimeException> notFoundExceptionClass
    ) {
        logger.log(level, "Error " + action, e);

        if (notFoundExceptionClass != null && notFoundExceptionClass.isInstance(e)) {
            throw notFoundExceptionClass.cast(e);
        }

        if (serviceExceptionClass.isInstance(e)) {
            throw serviceExceptionClass.cast(e);
        }

        try {
            throw serviceExceptionClass
                    .getConstructor(String.class, Throwable.class)
                    .newInstance("Service error while " + action, e);
        } catch (Exception reflectionError) {
            throw new RuntimeException("Unexpected error during exception handling", reflectionError);
        }
    }
}