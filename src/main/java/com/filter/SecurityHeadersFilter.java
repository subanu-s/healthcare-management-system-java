package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class SecurityHeadersFilter
        implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException,
            ServletException {

        HttpServletResponse resp =
                (HttpServletResponse) response;

        resp.setHeader(
                "X-Content-Type-Options",
                "nosniff"
        );

        resp.setHeader(
                "X-Frame-Options",
                "DENY"
        );

        resp.setHeader(
                "Referrer-Policy",
                "strict-origin-when-cross-origin"
        );

        resp.setHeader(
                "Cache-Control",
                "no-store, no-cache, must-revalidate"
        );

        resp.setHeader(
                "Pragma",
                "no-cache"
        );

        chain.doFilter(
                request,
                response
        );
    }
}