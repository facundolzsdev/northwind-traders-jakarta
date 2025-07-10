package com.northwind.filters;

import com.northwind.util.jsf.SessionUtil;
import com.northwind.util.validation.RouteValidator;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static com.northwind.util.constants.ViewPaths.*;

/**
 * Redirects unauthenticated users to the login page.
 * If the session has expired, appends an "expired=true" flag to the redirect.
 * Allows access to public paths without authentication.
 */
@WebFilter("/*")
public class SessionExpiredFilter implements Filter {

    private static final String ATTR_LOGGED_USER = "loggedUser";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (isPublicPath(req)) {
            chain.doFilter(request, response);
            return;
        }

        if (!isUserLoggedIn(req)) {
            handleUnauthenticatedRequest(req, res);
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * Checks if the requested URI corresponds to a public path
     * that doesn't require user authentication.
     *
     * @param request the HTTP request
     * @return true if the path is public, false otherwise
     */
    private boolean isPublicPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String path = request.getRequestURI().replaceFirst(contextPath, "");
        return RouteValidator.isPublicPath(path);
    }

    /**
     * Determines whether a user is currently logged in
     * by checking the session for a "loggedUser" attribute.
     *
     * <p>Note: This method does not use {@link SessionUtil#getLoggedUserFromSession()}
     * or similar utility methods because filters operate at a lower level of the application
     * lifecycle and do not have access to JSF or CDI context.</p>
     *
     * @param request the HTTP request
     * @return true if a valid user session exists, false otherwise
     */
    private boolean isUserLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(ATTR_LOGGED_USER) != null;
    }

    /**
     * Handles requests from unauthenticated users.
     * Redirects to the login page, optionally appending an "expired=true" flag
     * if the request is not the initial entry point to the app.
     *
     * @param req the HTTP request
     * @param res the HTTP response
     * @throws IOException if redirect fails
     */
    private void handleUnauthenticatedRequest(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        if (isInitialRequest(req)) {
            res.sendRedirect(req.getContextPath() + Common.LOGIN);
        } else {
            prepareExpiredSessionRedirect(req, res);
        }
    }

    /**
     * Checks if the request is the user's first access to the app.
     * (i.e., no Referer header or not from the same context).
     *
     * @param req the HTTP request
     * @return true if it's an initial request, false if it's a navigation within the app
     */
    private boolean isInitialRequest(HttpServletRequest req) {
        String referer = req.getHeader("Referer");
        return referer == null || !referer.contains(req.getContextPath());
    }

    /**
     * Prepares a redirect to the login page for an expired session.
     * Sets the "expired=true" flag in the redirect URL and adds a flash scope message.
     *
     * @param req the HTTP request
     * @param res the HTTP response
     * @throws IOException if redirect fails
     */
    private void prepareExpiredSessionRedirect(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            facesContext.getExternalContext().getFlash().put("expired", true);
            facesContext.getExternalContext().getFlash().setKeepMessages(true);
        }
        res.sendRedirect(req.getContextPath() + Common.LOGIN + Redirect.EXPIRED);
    }
}
