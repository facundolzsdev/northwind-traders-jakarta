package com.northwind.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Sets caching headers for static resources to improve performance.
 * Applies to resources served under /jakarta.faces.resource and /resources.
 */
@WebFilter({"/jakarta.faces.resource/*", "/resources/*"})
public class StaticResourcesFilter implements Filter {

    private static final long MAX_AGE = 86_400;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Cache-Control", "public, max-age=" + MAX_AGE);
        response.setHeader("Pragma", "cache");

        chain.doFilter(req, res);
    }
}
