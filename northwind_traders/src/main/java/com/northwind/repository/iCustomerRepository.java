package com.northwind.repository;

import com.northwind.model.entity.Customer;

/**
 * Repository interface for performing database operations related to {@link Customer}.
 * <p>
 * Extends {@code iCrudRepository} to provide standard CRUD functionality.
 * </p>
 */
public interface iCustomerRepository extends iCrudRepository<Customer, Integer> {

    /**
     * Finds a customer by the associated user ID.
     *
     * @param userId the ID of the user linked to the customer
     * @return the customer associated with the given user ID
     */
    Customer findByUserId(Integer userId);

    /**
     * Finds a customer by ID including the associated user.
     *
     * @param id the ID of the customer
     * @return the customer with its associated user, or null if not found
     */
    Customer findByIdWithUser(Integer id);

    /**
     * Finds a customer by their associated user's ID, including the user entity.
     *
     * @param userId the ID of the associated user
     * @return the customer with its associated user, or null if not found
     */
    Customer findCustomerWithUserByUserId(Integer userId);

}
