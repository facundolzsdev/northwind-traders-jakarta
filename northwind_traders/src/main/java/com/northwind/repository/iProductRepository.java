package com.northwind.repository;

import com.northwind.model.entity.Product;

import java.util.List;

/**
 * Repository interface for performing database operations related to {@link Product}.
 * <p>
 * Extends {@code iCrudRepository} to provide standard CRUD functionality.
 * </p>
 */
public interface iProductRepository extends iCrudRepository<Product, Integer> {

    List<Product> findProductByName(String name);

}
