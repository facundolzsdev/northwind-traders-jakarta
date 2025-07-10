package com.northwind.repository;

import com.northwind.model.entity.Admin;

/**
 * Repository interface for performing database operations related to {@link Admin}.
 * <p>
 * Extends {@code iCrudRepository} to provide standard CRUD functionality.
 * </p>
 */
public interface iAdminRepository extends iCrudRepository<Admin, Integer> {

    /**
     * Finds an Admin by their associated user's ID, including the user entity.
     *
     * @param userId the ID of the associated user
     * @return the admin with its associated user, or null if not found
     */
    Admin findAdminWithUserByUserId(Integer userId);

    /**
     * Finds an admin by ID including the associated user.
     *
     * @param id the ID of the admin
     * @return the admin with its associated user, or null if not found
     */
    Admin findByIdWithUser(Integer id);
}
