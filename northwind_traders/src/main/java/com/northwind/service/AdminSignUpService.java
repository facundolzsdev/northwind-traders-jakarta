package com.northwind.service;

import com.northwind.exceptions.SignUpServiceException;
import com.northwind.model.entity.Admin;
import com.northwind.model.support.Audit;
import com.northwind.model.entity.User;
import com.northwind.model.enums.RoleType;
import com.northwind.service.factory.UserFactory;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class AdminSignUpService {

    @Inject
    private iUserService userService;
    @Inject
    private iAdminService adminService;
    @Inject
    private UserFactory userFactory;
    @Inject
    private Logger logger;

    public void registerAdmin(String username, String email, String password, Admin admin) {
        try {
            User user = userFactory.createUser(username, email, password);
            userService.registerUser(user, RoleType.ADMIN);

            admin.setUser(user);
            admin.setAudit(Audit.createDefaultAudit());
            adminService.saveAdmin(admin);

            logger.log(Level.INFO, "Admin registered successfully: {0}", user.getUsername());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during Admin registration: " + username, e);
            throw new SignUpServiceException("An unexpected error occurred during Admin registration.", e);
        }
    }

}
