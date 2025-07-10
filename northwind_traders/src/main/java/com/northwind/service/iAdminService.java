package com.northwind.service;

import com.northwind.model.entity.Admin;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for handling admin-related business logic.
 */
public interface iAdminService {

    List<Admin> getAllAdmins();

    Optional<Admin> getAdminById(Integer id);

    void saveAdmin(Admin admin);

    void deleteAdmin(Integer id);

    Optional<Admin> getAdminWithUserByUserId(Integer userId);

    Optional<Admin> getByIdWithUser(Integer id);
}
