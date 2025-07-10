package com.northwind.service;

import com.northwind.exceptions.ProductNotFoundException;
import com.northwind.model.entity.Category;
import com.northwind.model.entity.Product;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for handling product-related business logic.
 */
public interface iProductService {

    Optional<Product> getProductById(Integer id);

    List<Product> getAllProducts();

    void saveProduct(Product product);

    void deleteProduct(Integer id);

    List<Product> getProductByName(String name);

    // ** SERVICE-LAYER SPECIFIC METHODS ** //

    List<Category> getAllCategories();

    Optional<Category> getCategoryById(Byte id);

    /**
     * Updates the specified product and records the update operation for auditing purposes.
     *
     * @param product the product to be updated
     */
    void updateProductWithAudit(Product product);

    /**
     * Retrieves the product with the specified ID.
     * Throws an exception if no product is found.
     *
     * @param id the ID of the product
     * @return the product with the given ID
     * @throws ProductNotFoundException if no product is found with the given ID
     */
    Product requireProductById(Integer id);

}



