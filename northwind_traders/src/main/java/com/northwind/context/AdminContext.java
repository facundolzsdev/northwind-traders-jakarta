package com.northwind.context;

import com.northwind.exceptions.AuthServiceException;
import com.northwind.model.entity.Admin;
import com.northwind.model.entity.User;
import com.northwind.model.enums.RoleType;
import com.northwind.service.iAdminService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.logging.Logger;

@Named
@SessionScoped
public class AdminContext implements Serializable {

    @Inject
    private AuthenticatedUserContext authContext;
    @Inject
    private iAdminService adminService;
    @Inject
    private Logger logger;

    /**
     * Returns the ID of the currently authenticated admin user, or {@code null}
     * if the user is not authenticated or does not have the ADMIN role.
     *
     * @return the admin user's ID, or {@code null} if not applicable
     */
    public Integer getAuthenticatedAdminUserId() {
        User user = authContext.getLoggedUser();
        if (user != null && user.hasRole(RoleType.ADMIN)) {
            return user.getId();
        }
        return null;
    }

    /**
     * Retrieves the {@link Admin} entity associated with the currently authenticated user.
     *
     * @return the corresponding {@link Admin}
     * @throws AuthServiceException if no authenticated user is found or if the user
     *                              is not assigned the ADMIN role
     */
    public Admin requireAdmin() {
        User user = authContext.getLoggedUser();
        if (user == null || !user.hasRole(RoleType.ADMIN)) {
            throw new AuthServiceException("No authenticated Admin found");
        }

        return adminService.getAdminWithUserByUserId(user.getId())
                .orElseThrow(() -> new AuthServiceException
                        ("No Admin entity found for authenticated user (ID: " + user.getId() + ")"));
    }

}
