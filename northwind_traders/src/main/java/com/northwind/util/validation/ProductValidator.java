package com.northwind.util.validation;

import com.northwind.model.entity.Product;

import static com.northwind.util.constants.messages.GrowlTitles.*;

import static com.northwind.util.constants.messages.ProductMessages.*;

import com.northwind.util.jsf.GrowlUtil;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;

/**
 * Utility class for validating Product-related fields.
 * <p>
 * Checks for completeness and valid values before allowing operations.
 * </p>
 */
@ApplicationScoped
public class ProductValidator {

    /**
     * Validates the entire product object.
     * Shows warning messages if validation fails.
     *
     * @param product the product to validate
     * @return true if the product is invalid, false otherwise
     */
    public boolean isProductInvalid(Product product) {
        if (isFormIncomplete(product)) {
            GrowlUtil.warning(ERROR_OPERATION_FAILED, WARN_INCOMPLETE_FORM_FIELDS);
            return true;
        }

        if (!validatePrice(product.getPrice())) {
            GrowlUtil.warning(ERROR_OPERATION_FAILED, WARN_INVALID_PRICE);
            return true;
        }

        if (violatesActivationStockRule(product)) {
            if (product.isActive()) {
                GrowlUtil.warning(ERROR_OPERATION_FAILED, ERROR_ACTIVE_PRODUCT_NO_STOCK);
            } else {
                GrowlUtil.warning(ERROR_OPERATION_FAILED, ERROR_INACTIVE_PRODUCT_HAS_STOCK);
            }
            return true;
        }

        return false;
    }

    public boolean validatePrice(BigDecimal price) {
        return price != null && price.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isFormIncomplete(Product product) {
        return isNameInvalid(product) || isUnitInvalid(product) || isCategoryInvalid(product);
    }

    /**
     * Validates the business rule:
     * - Active products must have stock > 0
     * - Inactive products must have stock = 0
     *
     * @param product the product to check
     * @return true if the rule is violated, false otherwise
     */
    public boolean violatesActivationStockRule(Product product) {
        if (product.isActive()) {
            return product.getStock() <= 0;
        } else {
            return product.getStock() > 0;
        }
    }

    private boolean isNameInvalid(Product product) {
        return product.getName() == null || product.getName().trim().isEmpty();
    }

    private boolean isUnitInvalid(Product product) {
        return product.getUnit() == null || product.getUnit().trim().isEmpty();
    }

    private boolean isCategoryInvalid(Product product) {
        return product.getCategory() == null;
    }

}

