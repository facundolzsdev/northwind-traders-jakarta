package com.northwind.service;

import com.northwind.model.entity.Role;
import com.northwind.model.enums.RoleType;

import java.util.Optional;

/**
 * Service interface for handling role-related business logic.
 */
public interface iRoleService {

    /**
     * Retrieves a role by its type.
     *
     * @param type The type of role to search for.
     * @return An {@link Optional} containing the role if found, or empty if it does not exist.
     */
    Optional<Role> getRoleByType(RoleType type);
}
