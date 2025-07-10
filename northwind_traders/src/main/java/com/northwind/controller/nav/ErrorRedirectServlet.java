package com.northwind.controller.nav;

import com.northwind.util.constants.ViewPaths;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet that redirects all GET requests to a custom JSF error page.
 * <p>
 * This is useful for redirecting from unexpected errors or broken navigation flows
 * to a user-friendly error page.
 */
@WebServlet("/redirectToError")
public class ErrorRedirectServlet extends HttpServlet {

    /**
     * Handles GET requests by redirecting to the JSF error page.
     *
     * @param req  The HTTP request
     * @param resp The HTTP response
     * @throws IOException If redirection fails
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String contextPath = req.getContextPath();
        resp.sendRedirect(contextPath + ViewPaths.Common.ERROR_CUSTOM);
    }
}