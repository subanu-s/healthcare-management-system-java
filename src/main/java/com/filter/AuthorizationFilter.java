package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(
        urlPatterns = {
                "/patient/*",
                "/doctor/*",
                "/admin/*",
                "/consultation/*"
        }
)
public class AuthorizationFilter
        implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException,
            ServletException {

        HttpServletRequest req =
                (HttpServletRequest) request;

        HttpServletResponse resp =
                (HttpServletResponse) response;

        HttpSession session =
                req.getSession(false);

        String role =
                session == null
                        ? null
                        : (String)
                        session.getAttribute(
                                "AUTH_ROLE"
                        );

        String path =
                req.getRequestURI()
                        .substring(
                                req.getContextPath()
                                        .length()
                        );

        if (path.startsWith(
                "/patient/")) {

            if (!"PATIENT".equals(role)) {

                resp.sendRedirect(
                        req.getContextPath()
                        + "/user_login.jsp"
                );

                return;
            }
        }

        if (path.startsWith(
                "/doctor/")) {

            if (!"DOCTOR".equals(role)) {

                resp.sendRedirect(
                        req.getContextPath()
                        + "/doctor_login.jsp"
                );

                return;
            }
        }

        if (path.startsWith(
                "/admin/")) {

            if (!"ADMIN".equals(role)) {

                resp.sendRedirect(
                        req.getContextPath()
                        + "/admin_login.jsp"
                );

                return;
            }
        }

        if (path.startsWith(
                "/consultation/")) {

            if (!"PATIENT".equals(role)
                    && !"DOCTOR".equals(role)) {

                resp.sendRedirect(
                        req.getContextPath()
                        + "/index.jsp"
                );

                return;
            }
        }

        chain.doFilter(
                request,
                response
        );
    }
}