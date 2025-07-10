package com.northwind.controller.nav;

import com.northwind.model.entity.User;

import static com.northwind.util.constants.ViewPaths.*;

import com.northwind.util.jsf.SessionUtil;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles post-error recovery navigation by redirecting users
 * to their role-specific entry points.
 * <p>
 * Used primarily by error pages to provide a safe return path.
 */
@Named("recoveryNavigator")
@RequestScoped
public class RecoveryNavigatorBean implements Serializable {

    @Inject
    private Logger logger;

    /**
     * Provides a role-appropriate return destination after errors.
     * Usage: Bind to "Back" button in error pages.
     *
     * @return Faces redirect path to user's dashboard or login page
     */
    public String navigateToRecoveryDestination() {
        try {
            User user = SessionUtil.getLoggedUserFromSession();

            if (user != null && user.getRole() != null) {
                return switch (user.getRole().getName()) {
                    case ADMIN -> Admin.DASHBOARD + Redirect.FACES;
                    case EMPLOYEE -> Employee.DASHBOARD + Redirect.FACES;
                    case CUSTOMER -> Customer.PRODUCTS + Redirect.FACES;
                    default -> Common.LOGIN + Redirect.FACES;
                };
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error determining recovery destination", e);
        }
        return Common.LOGIN + Redirect.FACES;
    }

}
