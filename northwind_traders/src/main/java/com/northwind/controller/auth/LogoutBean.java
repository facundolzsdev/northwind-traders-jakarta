package com.northwind.controller.auth;

import com.northwind.util.jsf.NavigationUtil;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.logging.Logger;

/**
 * Managed bean responsible for handling user logout.
 */
@Named
@RequestScoped
public class LogoutBean {

    @Inject
    private Logger logger;

    /**
     * Invalidates the current session and redirects the user to the login page.
     *
     * @return Navigation string to redirect to login page
     */
    public String logout() {
        NavigationUtil.invalidateSessionAndRedirectToLogin();
        return null;
    }

}