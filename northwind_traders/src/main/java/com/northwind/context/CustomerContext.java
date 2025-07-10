package com.northwind.context;

import com.northwind.exceptions.AuthServiceException;
import com.northwind.model.entity.Customer;
import com.northwind.model.entity.User;
import com.northwind.model.enums.RoleType;
import com.northwind.service.iCustomerService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Provides access to the currently authenticated customer within the session context.
 * This class encapsulates logic specific to customers, such as retrieving the
 * associated {@link Customer} entity or the user's ID, and ensures the session-bound
 * user has the correct role.
 *
 * <p>
 * Designed for injection into beans that require customer-related context or access control.
 * Helps enforce role-based restrictions and simplifies customer retrieval logic.
 * </p>
 *
 * @see AuthenticatedUserContext
 */
@Named
@SessionScoped
public class CustomerContext implements Serializable {

    @Inject
    private AuthenticatedUserContext authContext;
    @Inject
    private iCustomerService customerService;
    @Inject
    private Logger logger;

    /**
     * Returns the ID of the currently authenticated customer user, or {@code null}
     * if the user is not authenticated or does not have the CUSTOMER role.
     *
     * @return the customer user's ID, or {@code null} if not applicable
     */
    public Integer getAuthenticatedCustomerUserId() {
        User user = authContext.getLoggedUser();
        if (user != null && user.hasRole(RoleType.CUSTOMER)) {
            return user.getId();
        }
        return null;
    }

    /**
     * Retrieves the {@link Customer} entity associated with the currently authenticated user.
     *
     * @return the corresponding {@link Customer}
     * @throws AuthServiceException if no authenticated user is found or if the user
     *                              is not assigned the CUSTOMER role
     */
    public Customer requireCustomer() {
        User user = authContext.getLoggedUser();
        if (user == null || !user.hasRole(RoleType.CUSTOMER)) {
            throw new AuthServiceException("No authenticated customer found");
        }

        return customerService.getCustomerWithUserByUserId(user.getId())
                .orElseThrow(() -> new AuthServiceException
                        ("No Customer entity found for authenticated user (ID: " + user.getId() + ")"));
    }
}
