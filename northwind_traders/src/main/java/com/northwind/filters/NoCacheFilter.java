package com.northwind.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Filter that disables browser caching for secure views,
 * ensuring users cannot return to protected pages after logout.
 */
@WebFilter({"/views/admin/*", "/views/employee/*", "/views/customer/*"})
public class NoCacheFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResp = (HttpServletResponse) response;
        httpResp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResp.setHeader("Pragma", "no-cache");
        httpResp.setDateHeader("Expires", 0);

        chain.doFilter(request, response);
    }
}
