package com.northwind.util.validation;

import com.northwind.model.entity.Product;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.experimental.UtilityClass;

/**
 * Utility class for cart-related validations.
 * <p>
 * Provides methods for quantity normalization and stock availability checks.
 * </p>
 */
@ApplicationScoped
@UtilityClass
public class CartValidatorUtil {

    /**
     * Ensures that the quantity is at least 1.
     *
     * @param quantity the requested quantity
     * @return the normalized quantity (minimum of 1)
     */
    public static short normalizeQuantity(short quantity) {
        return (short) Math.max(quantity, 1);
    }

    /**
     * Checks if the requested quantity is available in stock.
     *
     * @param product  the product to check
     * @param quantity the requested quantity
     * @return true if the product has enough stock, false otherwise
     */
    public static boolean isStockAvailable(Product product, short quantity) {
        return quantity <= product.getStock();
    }

}
