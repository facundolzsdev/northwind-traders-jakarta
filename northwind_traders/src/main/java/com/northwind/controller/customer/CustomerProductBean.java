package com.northwind.controller.customer;

import com.northwind.controller.base.BaseProductBean;
import com.northwind.model.entity.Product;
import jakarta.faces.view.ViewScoped;

import jakarta.inject.Named;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Managed bean for customer-facing product views.
 * <p>
 * Handles filtering of products by name using shared logic from {@link BaseProductBean}.
 * </p>
 */
@Getter
@Setter
@Named
@ViewScoped
public class CustomerProductBean extends BaseProductBean implements Serializable {

    /**
     * Returns only the products that have stock available (> 0).
     *
     * @return list of products in stock
     */
    @Override
    public List<Product> getProducts() {
        return super.getProducts().stream()
                .filter(p -> p.getStock() > 0)
                .collect(Collectors.toList());
    }

}