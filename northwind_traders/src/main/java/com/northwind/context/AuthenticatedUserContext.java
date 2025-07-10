package com.northwind.context;

import com.northwind.exceptions.AuthServiceException;
import com.northwind.model.entity.User;
import com.northwind.model.enums.RoleType;
import com.northwind.service.iCustomerService;

import static com.northwind.util.constants.ViewPaths.*;

import com.northwind.util.jsf.SessionUtil;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides contextual access to the currently authenticated user from the session.
 * Centralizes logic to retrieve and validate session-bound user information.
 *
 * @see SessionUtil
 */
@Named
@SessionScoped
public class AuthenticatedUserContext implements Serializable {

    @Inject
    private iCustomerService customerService;
    @Inject
    private Logger logger;

    /**
     * Retrieves the currently authenticated user's ID from the session.
     *
     * @return the user ID, or null if not present
     */
    public Integer getUserId() {
        return SessionUtil.getUserIdFromSession();
    }

    /**
     * Retrieves the currently authenticated User object from the session.
     *
     * @return the User, or null if not present
     */
    public User getLoggedUser() {
        return SessionUtil.getLoggedUserFromSession();
    }

    /**
     * Executes the given action if a user ID is available in the current session.
     *
     * @param action a {@code Consumer} to execute with the user ID if present
     */
    public void ifUserIdPresent(Consumer<Integer> action) {
        Optional.ofNullable(getUserId()).ifPresent(action);
    }

    /**
     * Returns the currently authenticated User, or throws an AuthServiceException if not found.
     *
     * @return the authenticated User
     * @throws AuthServiceException if no user is authenticated
     */
    public User requireUser() {
        User user = getLoggedUser();
        if (user == null) {
            logger.severe("Attempted access without authenticated user in session.");
            throw new AuthServiceException("No user logged in (internal use)");
        }
        return user;
    }

    /**
     * Determines the home page path based on the authenticated user's role.
     *
     * @return the path to the home page, or the login page if no user is authenticated
     */
    public String getHomePage() {
        User user = getLoggedUser();
        if (user == null) {
            return Common.LOGIN + Redirect.FACES;
        }

        return switch (user.getRole().getName()) {
            case CUSTOMER -> Customer.PRODUCTS;
            case ADMIN -> Admin.DASHBOARD;
            case EMPLOYEE -> Employee.DASHBOARD;
        };
    }

    /**
     * Returns the configuration page path based on user role.
     *
     * @return Path with redirect, or null if not authorized
     */
    public String getConfigurationPage() {
        User user = getLoggedUser();
        if (user == null || user.getRole() == null) {
            return null;
        }

        return switch (user.getRole().getName()) {
            case CUSTOMER -> Customer.PERSONAL_INFO_EDIT + Redirect.FACES;
            case ADMIN -> Common.USER_ACCOUNT_EDIT + Redirect.FACES;
            default -> null;
        };
    }

    /**
     * Returns true if the user has the CUSTOMER role.
     */
    public boolean isCustomer() {
        return hasRole("CUSTOMER");
    }

    /**
     * Returns true if the user has the EMPLOYEE role.
     */
    public boolean isEmployee() {
        return hasRole("EMPLOYEE");
    }

    /**
     * Returns true if the user has the ADMIN role.
     */
    public boolean isAdmin() {
        return hasRole("ADMIN");
    }

    /**
     * Checks if the authenticated user has the given role.
     * Accepts either the enum name (e.g., "CUSTOMER") or the label (e.g., "Customer").
     *
     * @param role the role to check
     * @return true if the user has the role, false otherwise
     */
    private boolean hasRole(String role) {
        try {
            RoleType roleType;
            try {
                roleType = RoleType.valueOf(role.toUpperCase());
            } catch (IllegalArgumentException e) {
                roleType = RoleType.fromLabel(role);
            }

            return getLoggedUser() != null &&
                    getLoggedUser().getRole() != null &&
                    getLoggedUser().getRole().getName() == roleType;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error verifying role: " + role, e);
            return false;
        }
    }

}
