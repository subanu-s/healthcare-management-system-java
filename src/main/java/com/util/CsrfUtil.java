package com.util;

import java.security.SecureRandom;
import java.util.Base64;

import javax.servlet.http.HttpSession;

public final class CsrfUtil {

    private static final SecureRandom RANDOM =
            new SecureRandom();

    private CsrfUtil() {
    }

    public static String token(
            HttpSession session) {

        Object existing =
                session.getAttribute(
                        "CSRF_TOKEN"
                );

        if (existing != null) {
            return existing.toString();
        }

        byte[] data =
                new byte[24];

        RANDOM.nextBytes(data);

        String token =
                Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(data);

        session.setAttribute(
                "CSRF_TOKEN",
                token
        );

        return token;
    }
}