package com.northwind.util.exception;

import com.northwind.util.jsf.GrowlUtil;
import com.northwind.util.constants.messages.GrowlTitles;
import lombok.experimental.UtilityClass;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for handling exceptions in JSF Managed beans.
 * Logs the exception and displays a user-friendly message using {@link GrowlUtil}.
 */
@UtilityClass
public class BeanExceptionUtil {

    /**
     * Handles an exception in a bean by logging it and showing a predefined user-friendly message.
     *
     * @param logger      Logger used to log the error.
     * @param action      Description of the action attempted (e.g. "deleting product").
     * @param e           The exception that occurred.
     * @param userMessage The message to display to the user (typically from a message constants class).
     * @param logLevel    The level at which to log the error.
     */
    public static void handle(Logger logger, String action, Exception e, String userMessage, Level logLevel) {
        logger.log(logLevel, "Error during " + action, e);
        GrowlUtil.error(GrowlTitles.ERROR_OPERATION_FAILED, userMessage);
    }
}

