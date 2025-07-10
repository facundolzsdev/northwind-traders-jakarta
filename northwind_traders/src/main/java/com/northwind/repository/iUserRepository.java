package com.northwind.repository;

import com.northwind.model.entity.User;

/**
 * Repository interface for performing database operations related to {@link User}.
 * <p>
 * Extends {@code iCrudRepository} to provide standard CRUD functionality.
 * </p>
 */
public interface iUserRepository extends iCrudRepository<User, Integer> {

    User findUserByUsernameOrEmail(String identifier);

    boolean usernameExists(String username);

    boolean emailExists(String email);
}
