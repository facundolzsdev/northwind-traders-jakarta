package com.northwind.service;

import com.northwind.exceptions.AuthServiceException;
import com.northwind.model.entity.User;

import java.util.Optional;

/**
 * Authentication service interface that defines user login behavior.
 * Provides contract for verifying credentials using either username or email.
 */
public interface iAuthService {

    /**
     * Attempts to authenticate a user using provided credentials.
     *
     * @param identifier Username or email
     * @param password   Plain-text password
     * @return Optional with User if authenticated, empty otherwise
     * @throws AuthServiceException for authentication failures (including inactive employees)
     */
    Optional<User> login(String identifier, String password) throws AuthServiceException;
}
