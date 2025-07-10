package com.northwind.controller.base;

import com.northwind.model.entity.Category;
import com.northwind.model.entity.Product;
import com.northwind.service.iProductService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Abstract base bean for for product-related backing beans.
 * <p>
 * Encapsulates shared logic and common fields used in both customer and employee product views,
 * such as listing, searching, and resetting products.
 * </p>
 *
 * <p>
 * Subclasses must define view-specific behavior such as creation, editing, or filtering rules.
 * </p>
 */
@Getter
@Setter
@Named
@ViewScoped
public abstract class BaseProductBean implements Serializable {

    protected Product product;
    protected List<Product> products;
    protected String searchQuery;

    @Inject
    protected iProductService productService;

    /**
     * Initializes the product list and resets the current product.
     * Subclasses may override this method to perform additional setup logic.
     */
    @PostConstruct
    public void init() {
        loadProducts();
        resetProduct();
    }

    public List<Category> getCategories() {
        return productService.getAllCategories();
    }

    /**
     * Filters the product list based on the search query.
     * This method uses the product service to fetch products matching the query string.
     */
    public void find() {
        this.products = productService.getProductByName(searchQuery);
    }

    /**
     * Resets the search field and reloads the full list of products.
     */
    public void reloadTable() {
        this.searchQuery = "";
        refreshProductList();
    }

    /**
     * Refreshes the product list and resets the current product.
     */
    protected void refreshProductList() {
        loadProducts();
        resetProduct();
    }

    /**
     * Loads all products from the product service.
     */
    protected void loadProducts() {
        this.products = productService.getAllProducts();
    }

    /**
     * Initializes a new empty product instance.
     */
    protected void resetProduct() {
        this.product = new Product();
    }
}