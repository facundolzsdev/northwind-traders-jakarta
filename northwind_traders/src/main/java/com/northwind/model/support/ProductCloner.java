package com.northwind.model.support;

import com.northwind.model.entity.Product;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductCloner {

    /**
     * Clones a Product instance.
     *
     * @param original the product to clone
     * @return a new Product with the same field values
     */
    public static Product clone(Product original) {
        if (original == null) {
            return null;
        }

        Product copy = new Product();
        copy.setId(original.getId());
        copy.setName(original.getName());
        copy.setCategory(original.getCategory());
        copy.setPrice(original.getPrice());
        copy.setUnit(original.getUnit());
        copy.setStock(original.getStock());
        copy.setActive(original.isActive());
        return copy;
    }
}