package com.northwind.util.validation;

import com.northwind.exceptions.ValidationException;
import com.northwind.model.entity.Order;
import com.northwind.util.constants.messages.OrderMessages;
import lombok.experimental.UtilityClass;

/**
 * Utility class for order review validations.
 * <p>
 * Provides methods to ensure required order identifiers are present.
 * </p>
 */
@UtilityClass
public class OrderReviewValidator {

    /**
     * Ensures that the selected order is not null.
     *
     * @param selectedOrder the order selected for review
     * @throws ValidationException if the order is null
     */
    public static void requireSelectedOrder(Order selectedOrder) {
        if (selectedOrder == null) {
            throw new ValidationException(OrderMessages.ORDER_NOT_FOUND_ERROR);
        }
    }

    /**
     * Ensures that the provided order ID is not null.
     *
     * @param orderId the ID of the order
     * @throws ValidationException if the order ID is null
     */
    public static void requireOrderId(Integer orderId) {
        if (orderId == null) {
            throw new ValidationException(OrderMessages.ORDER_NOT_FOUND_ERROR);
        }
    }
}