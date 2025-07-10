package com.northwind.util.jsf;

import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.experimental.UtilityClass;

import static com.northwind.util.constants.ViewPaths.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for handling page redirections.
 * <p>
 * Provides static methods to safely redirect to specific pages,
 * including a fallback to a predefined error page in case of failures.
 * </p>
 */
@UtilityClass
public class NavigationUtil {

    private static final Logger logger = Logger.getLogger(NavigationUtil.class.getName());

    public static void redirectToErrorPage() {
        ExternalContext ec = getExternalContext();
        if (ec == null) {
            logger.severe("ExternalContext is null. Cannot perform redirect to error page.");
            return;
        }
        try {
            ec.redirect(ec.getRequestContextPath() + Common.ERROR_CUSTOM);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error redirecting to error page", e);
        }
    }

    /**
     * Invalidates the current session, removes the JSESSIONID cookie,
     * and redirects the user to the login page.
     * <p>
     * Used during logout or session expiration handling.
     * </p>
     */
    public static void invalidateSessionAndRedirectToLogin() {
        ExternalContext ec = getExternalContext();
        if (ec == null) {
            logger.severe("ExternalContext is null. Cannot invalidate session or redirect.");
            return;
        }

        try {
            HttpServletRequest request = (HttpServletRequest) ec.getRequest();
            HttpServletResponse response = (HttpServletResponse) ec.getResponse();

            invalidateSession(request);
            removeJSessionIdCookie(request, response);
            redirectToLogin(ec);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error during session invalidation and redirect to login page", e);
            redirectToErrorPage();
        }
    }

    public static void redirectToLogin() {
        ExternalContext ec = getExternalContext();
        if (ec == null) {
            logger.severe("ExternalContext is null. Cannot perform redirect to login.");
            return;
        }

        try {
            String loginUrl = ec.getRequestContextPath() + Common.LOGIN + Redirect.FACES;
            ec.redirect(loginUrl);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error redirecting to login page", e);
            redirectToErrorPage();
        }
    }

    /**
     * Attempts to redirect to the given page and logs a custom error message if it fails.
     * Falls back to the custom error page unless already redirecting there.
     *
     * @param page         the relative path to redirect to
     * @param errorMessage the custom message to log in case of failure
     */
    public static void redirectToPageSafely(String page, String errorMessage) {
        ExternalContext ec = getExternalContext();
        if (ec == null) {
            logger.severe("ExternalContext is null. Cannot perform redirect to " + page);
            return;
        }

        try {
            ec.redirect(ec.getRequestContextPath() + page);
        } catch (IOException e) {
            logger.log(Level.SEVERE, errorMessage, e);
            if (!page.equals(Common.ERROR_CUSTOM)) {
                redirectToErrorPage();
            }
        }
    }

    public static void redirectToPageSafely(String page) {
        redirectToPageSafely(page, "Redirection failed to: " + page);
    }

    private static void removeJSessionIdCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setMaxAge(0);
        cookie.setPath(request.getContextPath());
        response.addCookie(cookie);
    }

    private static void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    private static void redirectToLogin(ExternalContext ec) throws IOException {
        String loginUrl = ec.getRequestContextPath() + Common.LOGIN + Redirect.FACES;
        ec.redirect(loginUrl);
    }

    private static ExternalContext getExternalContext() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null) {
            logger.severe("FacesContext is null.");
            return null;
        }
        return context.getExternalContext();
    }
}
