package com.northwind.controller.employee;

import com.northwind.controller.base.BaseProductBean;
import com.northwind.model.entity.Product;

import com.northwind.util.jsf.GrowlUtil;
import com.northwind.util.general.InputSanitizer;
import com.northwind.model.support.ProductCloner;

import static com.northwind.util.constants.messages.GrowlTitles.*;
import static com.northwind.util.constants.messages.ProductMessages.*;

import com.northwind.util.validation.ProductValidator;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.northwind.util.exception.BeanExceptionUtil.handle;

/**
 * Managed bean for employee-facing product views.
 * <p>
 * Handles creation, editing, and management of products. Inherits shared logic
 * such as listing and searching from {@link BaseProductBean}.
 * </p>
 */
@Getter
@Setter
@Named
@ViewScoped
public class EmployeeProductBean extends BaseProductBean implements Serializable {

    private Product selectedProduct;
    private Product backupProduct;

    @Inject
    private ProductValidator productValidator;
    @Inject
    private Logger logger;

    public String getDialogTitle() {
        return selectedProduct != null && selectedProduct.getId() != null ? "Editar producto" : "Crear producto";
    }
    /**
     * Saves the currently selected product.
     * <p>
     * If the product does not have an ID, it is considered new and will be created.
     * If it has an ID, the existing product will be updated.
     */
    public void saveProduct() {
        if (selectedProduct.getId() == null) {
            createProduct();
        } else {
            updateProduct();
        }
    }

    public void prepareCreate() {
        if (selectedProduct == null || selectedProduct.getId() != null) {
            selectedProduct = new Product();
        }
    }

    public void prepareEdit(Product product) {
        this.selectedProduct = product;
        this.backupProduct = ProductCloner.clone(product);
    }

    public void updateProduct() {
        if (noChangesDetected()) {
            GrowlUtil.warning(INFO_MESSAGE, String.format(NO_PRODUCT_DATA_CHANGES_DETECTED, selectedProduct.getName()));
            return;
        }

        if (productValidator.isProductInvalid(selectedProduct)) {
            restoreBackup();
            return;
        }

        sanitizeSelectedProductName();

        try {
            productService.saveProduct(selectedProduct);
            GrowlUtil.success(SUCCESS_OPERATION_COMPLETED, String.format(SUCCESS_UPDATE, selectedProduct.getName()));
        } catch (Exception e) {
            handle(logger, "editing product", e, ERROR_UPDATE, Level.SEVERE);
        }
    }

    public void createProduct() {
        if (productValidator.isProductInvalid(selectedProduct)) {
            return;
        }

        sanitizeSelectedProductName();

        try {
            productService.saveProduct(selectedProduct);
            GrowlUtil.success(SUCCESS_OPERATION_COMPLETED, String.format(SUCCESS_CREATION, selectedProduct.getName()));
            selectedProduct = null;
        } catch (Exception e) {
            handle(logger, "creating product", e, ERROR_CREATION, Level.SEVERE);
        }
    }

    private void restoreBackup() {
        if (backupProduct != null && selectedProduct != null) {
            selectedProduct.setName(backupProduct.getName());
            selectedProduct.setCategory(backupProduct.getCategory());
            selectedProduct.setPrice(backupProduct.getPrice());
            selectedProduct.setUnit(backupProduct.getUnit());
            selectedProduct.setStock(backupProduct.getStock());
            selectedProduct.setActive(backupProduct.isActive());
        }
    }

    private boolean noChangesDetected() {
        return selectedProduct.equals(backupProduct);
    }

    private void sanitizeSelectedProductName() {
        selectedProduct.setName(InputSanitizer.sanitizeText(selectedProduct.getName()));
    }
}
