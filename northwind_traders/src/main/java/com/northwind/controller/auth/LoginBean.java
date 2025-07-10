package com.northwind.controller.auth;

import com.northwind.exceptions.AuthServiceException;
import com.northwind.exceptions.InactiveEmployeeException;
import com.northwind.exceptions.InvalidCredentialsException;
import com.northwind.model.entity.User;
import com.northwind.service.iAuthService;

import com.northwind.util.jsf.GrowlUtil;
import com.northwind.util.jsf.NavigationUtil;
import com.northwind.util.jsf.SessionUtil;

import static com.northwind.util.constants.messages.AuthMessages.*;
import static com.northwind.util.constants.messages.GrowlTitles.*;
import static com.northwind.util.constants.ViewPaths.*;
import static com.northwind.util.exception.BeanExceptionUtil.*;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.*;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Managed bean responsible for handling user authentication.
 * Supports login via email or username, session initialization, and redirection based on user role.
 * <p>
 * This bean is session-scoped to preserve login state and enable session expiration detection.
 */
@Getter
@Setter
@Named
@SessionScoped
public class LoginBean implements Serializable {

    private String identifier;
    private String password;
    private String userType;

    @Inject
    private iAuthService authService;

    @Inject
    private Logger logger;

    /**
     * Authenticates user and handles login scenarios:
     * - Successful login: redirects to appropriate page
     * - Invalid credentials: shows warning message
     * - Inactive employee: shows specific warning
     * - Other errors: shows generic error message
     *
     * @return null (stays on login page) or redirects if successful
     */
    public String login() {
        try {
            return authenticateAndRedirect();
        } catch (InvalidCredentialsException e) {
            logger.log(Level.WARNING, "Invalid credentials attempt for login");
            GrowlUtil.warning(INFO_MESSAGE, ERROR_LOGIN);
            return null;
        } catch (InactiveEmployeeException e) {
            logger.log(Level.WARNING, "Inactive employee login attempt: " + identifier);
            GrowlUtil.warning(INFO_MESSAGE, INACTIVE_EMPLOYEE);
            return null;
        } catch (AuthServiceException e) {
            handleException(e);
            return null;
        } catch (Exception e) {
            handleException(e);
            return null;
        }
    }

    /**
     * Authenticates and redirects user.
     *
     * @throws InvalidCredentialsException if auth fails
     * @throws IllegalStateException       if redirection fails
     */
    private String authenticateAndRedirect() {
        User user = authService.login(identifier, password)
                .orElseThrow(InvalidCredentialsException::new);

        storeUserSession(user);
        NavigationUtil.redirectToPageSafely(
                determineRedirectPage(user),
                "Failed redirect after login for: " + identifier
        );
        return null;
    }

    /**
     * Checks whether the session has expired based on request parameters or flash scope.
     * If expired, displays a warning message to the user.
     */
    public void checkSessionExpired() {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean sessionExpired = context.getExternalContext().getRequestParameterMap().containsKey("expired")
                || Boolean.TRUE.equals(context.getExternalContext().getFlash().get("expired"));

        if (sessionExpired && !context.isPostback()) {
            GrowlUtil.warning(INFO_MESSAGE, ERROR_SESSION_EXPIRED);
            PrimeFaces.current().ajax().update("messages");
        }
    }

    private String determineRedirectPage(User user) {
        return switch (user.getRole().getName()) {
            case CUSTOMER -> Customer.PRODUCTS;
            case ADMIN -> Admin.DASHBOARD;
            case EMPLOYEE -> Employee.DASHBOARD;
            default -> null;
        };
    }

    private void storeUserSession(User user) {
        SessionUtil.storeUserSession(user);
    }

    private void handleException(Exception e) {
        handle(logger, "login", e, GENERIC_ERROR_LOGIN, Level.SEVERE);
    }

}

