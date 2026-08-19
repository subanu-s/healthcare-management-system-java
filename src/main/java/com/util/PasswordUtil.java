package com.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordUtil {

    private static final int ITERATIONS = 120000;
    private static final int KEY_LENGTH = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {
    }

    public static String hash(String password) {

        try {

            byte[] salt = new byte[16];
            RANDOM.nextBytes(salt);

            byte[] derived = pbkdf2(
                    password.toCharArray(),
                    salt,
                    ITERATIONS,
                    KEY_LENGTH
            );

            return "pbkdf2$"
                    + ITERATIONS + "$"
                    + Base64.getEncoder().encodeToString(salt)
                    + "$"
                    + Base64.getEncoder().encodeToString(derived);

        } catch (Exception e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }

    public static boolean matches(
            String password,
            String storedHash) {

        if (password == null || storedHash == null) {
            return false;
        }

        try {

            if (storedHash.startsWith("pbkdf2$")) {

                String[] parts =
                        storedHash.split("\\$");

                int iterations =
                        Integer.parseInt(parts[1]);

                byte[] salt =
                        Base64.getDecoder()
                                .decode(parts[2]);

                byte[] expected =
                        Base64.getDecoder()
                                .decode(parts[3]);

                byte[] actual =
                        pbkdf2(
                                password.toCharArray(),
                                salt,
                                iterations,
                                expected.length * 8
                        );

                return MessageDigest.isEqual(
                        expected,
                        actual
                );
            }

            /*
             * Backward compatibility:
             * your existing users/admin may still
             * have the previous SHA-256 hash.
             */
            if (storedHash.matches(
                    "[a-fA-F0-9]{64}")) {

                MessageDigest md =
                        MessageDigest.getInstance(
                                "SHA-256"
                        );

                byte[] digest =
                        md.digest(
                                password.getBytes(
                                        StandardCharsets.UTF_8
                                )
                        );

                StringBuilder sb =
                        new StringBuilder();

                for (byte b : digest) {
                    sb.append(
                            String.format("%02x", b)
                    );
                }

                return MessageDigest.isEqual(
                        sb.toString()
                                .getBytes(
                                        StandardCharsets.UTF_8
                                ),
                        storedHash.toLowerCase()
                                .getBytes(
                                        StandardCharsets.UTF_8
                                )
                );
            }

        } catch (Exception e) {
            return false;
        }

        return false;
    }

    public static boolean needsUpgrade(
            String storedHash) {

        return storedHash != null
                && !storedHash.startsWith(
                        "pbkdf2$"
                );
    }

    private static byte[] pbkdf2(
            char[] password,
            byte[] salt,
            int iterations,
            int keyLength)
            throws Exception {

        PBEKeySpec spec =
                new PBEKeySpec(
                        password,
                        salt,
                        iterations,
                        keyLength
                );

        try {

            return SecretKeyFactory
                    .getInstance(
                            "PBKDF2WithHmacSHA256"
                    )
                    .generateSecret(spec)
                    .getEncoded();

        } finally {
            spec.clearPassword();
        }
    }
}