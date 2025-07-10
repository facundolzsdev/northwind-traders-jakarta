package com.northwind.filters;

import com.northwind.model.entity.User;

import static com.northwind.util.constants.ViewPaths.*;

import com.northwind.util.jsf.SessionUtil;
import com.northwind.util.validation.RouteValidator;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Filter that restricts access to JSF pages based on user role.
 * Redirects to access denied page if user lacks required permissions.
 * Logs unauthorized access attempts.
 */
@WebFilter({"/views/admin/*", "/views/employee/*", "/views/customer/*", "*.jsf"})
public class RoleBasedAccessFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(RoleBasedAccessFilter.class.getName());

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String contextPath = request.getContextPath();
        String path = extractPath(request);

        if (RouteValidator.isPublicPath(path)) {
            chain.doFilter(req, res);
            return;
        }

        User user = getLoggedUser(request);
        if (user == null) {
            redirectToLogin(response, contextPath);
            return;
        }

        if (!hasAccess(user, path)) {
            logUnauthorizedAccess(user, path);
            redirectToAccessDenied(response, contextPath);
            return;
        }

        chain.doFilter(req, res);
    }

    /**
     * Extracts the request path by removing the context path from the URI.
     * Useful for consistent path validation and access control.
     */
    private String extractPath(HttpServletRequest request) {
        return request.getRequestURI().replace(request.getContextPath(), "");
    }

    /**
     * Retrieves the logged-in user from the current HTTP session.
     *
     * <p>Note: This method does not use {@link SessionUtil#getLoggedUserFromSession()}
     * because utility methods that rely on JSF or CDI context do not work correctly
     * within servlet filters.
     * Filters operate at a lower level of the application lifecycle and lack access to JSF context</p>
     *
     * @param request the HTTP servlet request
     * @return the User object from session or {@code null} if not logged in
     */
    private User getLoggedUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("loggedUser");
    }

    /**
     * Verifies if the user has access to the requested path based on their role.
     *
     * @param user the authenticated user
     * @param path the requested resource path
     * @return true if access is permitted, false otherwise
     */
    private boolean hasAccess(User user, String path) {
        return RouteValidator.hasAccess(user.getRole().getName(), path);
    }

    private void redirectToLogin(HttpServletResponse response, String contextPath) throws IOException {
        response.sendRedirect(contextPath + Common.LOGIN);
    }

    private void redirectToAccessDenied(HttpServletResponse response, String contextPath) throws IOException {
        response.sendRedirect(contextPath + Common.ERROR_ACCESS_DENIED);
    }

    private void logUnauthorizedAccess(User user, String path) {
        LOG.warning(String.format("Unauthorized access attempt. User: %s, Role: %s, Path: %s",
                user.getUsername(), user.getRole().getName(), path));
    }
}
