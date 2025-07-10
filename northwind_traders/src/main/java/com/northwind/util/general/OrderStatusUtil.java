package com.northwind.util.general;

import lombok.experimental.UtilityClass;

/**
 * Utility class for handling order status-related logic,
 * such as mapping statuses to UI severity levels.
 */
@UtilityClass
public class OrderStatusUtil {

    public static String getStatusSeverity(String status) {
        return switch (status) {
            case "PENDIENTE" -> "warning";
            case "COMPLETADO" -> "success";
            case "RECHAZADO" -> "danger";
            default -> "info";
        };
    }
}
