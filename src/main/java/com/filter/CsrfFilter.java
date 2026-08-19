package com.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.util.CsrfUtil;

@WebFilter("/*")
public class CsrfFilter
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
                req.getSession(true);

        String expected =
                CsrfUtil.token(session);

        if ("POST".equalsIgnoreCase(
                req.getMethod())) {

            String supplied =
                    req.getParameter(
                            "_csrf"
                    );

            if (supplied == null
                    || !MessageDigest
                    .isEqual(
                            expected.getBytes(
                                    StandardCharsets.UTF_8
                            ),
                            supplied.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    )) {

                resp.sendError(
                        HttpServletResponse
                                .SC_FORBIDDEN,
                        "Invalid request token. "
                        + "Refresh the page "
                        + "and try again."
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