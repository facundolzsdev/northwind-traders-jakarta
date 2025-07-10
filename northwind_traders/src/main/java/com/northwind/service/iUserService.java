package com.northwind.service;

import com.northwind.model.entity.User;
import com.northwind.model.enums.RoleType;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for handling user-related business logic.
 */
public interface iUserService {

    Optional<User> getUserById(Integer id);

    List<User> getAllUsers();

    void saveUser(User user);

    void deleteUser(Integer id);

    Optional<User> getUserByUsernameOrEmail(String identifier);

    boolean usernameExists(String username);

    boolean emailExists(String email);

    // ** SERVICE-LAYER SPECIFIC METHODS ** //

    void registerUser(User user, RoleType roleType);

    void updateUser(User user, boolean passwordChanged);
}
