package com.northwind.context;

import com.northwind.exceptions.AuthServiceException;
import com.northwind.model.entity.Employee;
import com.northwind.model.entity.User;
import com.northwind.model.enums.RoleType;
import com.northwind.service.iEmployeeService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Provides access to the currently authenticated employee within the session context.
 * This class encapsulates logic specific to employees, such as retrieving the
 * associated {@link Employee} entity or the user's ID, and ensures the session-bound
 * user has the correct role.
 *
 * <p>
 * Designed for injection into beans that require employee-related context or access control.
 * Helps enforce role-based restrictions and simplifies employee retrieval logic.
 * </p>
 *
 * @see AuthenticatedUserContext
 */
@Named
@SessionScoped
public class EmployeeContext implements Serializable {

    @Inject
    private AuthenticatedUserContext authContext;
    @Inject
    private iEmployeeService employeeService;
    @Inject
    private Logger logger;

    /**
     * Returns the ID of the currently authenticated employee user, or {@code null}
     * if the user is not authenticated or does not have the EMPLOYEE role.
     */
    public Integer getAuthenticatedEmployeeUserId() {
        User user = authContext.getLoggedUser();
        if (user != null && user.hasRole(RoleType.EMPLOYEE)) {
            return user.getId();
        }
        return null;
    }

    /**
     * Retrieves the {@link Employee} entity associated with the currently authenticated user.
     *
     * @return the corresponding {@link Employee}
     * @throws AuthServiceException if no authenticated user is found or if the user
     *                              is not assigned the EMPLOYEE role
     */
    public Employee requireEmployee() {
        User user = authContext.getLoggedUser();
        if (user == null || !user.hasRole(RoleType.EMPLOYEE)) {
            throw new AuthServiceException("No authenticated employee found");
        }

        return employeeService.getEmployeeWithUserByUserId(user.getId())
                .orElseThrow(() -> new AuthServiceException
                        ("No Employee entity found for authenticated user (ID: " + user.getId() + ")"));
    }
}