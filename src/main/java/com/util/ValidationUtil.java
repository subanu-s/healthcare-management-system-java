package com.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public final class ValidationUtil {

    private static final Pattern EMAIL =
            Pattern.compile(
                    "^[A-Za-z0-9._%+-]+"
                    + "@[A-Za-z0-9.-]+"
                    + "\\.[A-Za-z]{2,}$"
            );

    private static final Pattern PHONE =
            Pattern.compile(
                    "^[6-9][0-9]{9}$"
            );

    private ValidationUtil() {
    }

    public static String required(
            String value,
            int max,
            String field) {

        String cleaned =
                value == null
                        ? ""
                        : value.trim();

        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException(
                    field + " is required"
            );
        }

        if (cleaned.length() > max) {
            throw new IllegalArgumentException(
                    field + " is too long"
            );
        }

        return cleaned;
    }

    public static String optional(
            String value,
            int max) {

        String cleaned =
                value == null
                        ? ""
                        : value.trim();

        if (cleaned.length() > max) {
            throw new IllegalArgumentException(
                    "Input is too long"
            );
        }

        return cleaned;
    }

    public static String email(
            String value) {

        String cleaned =
                required(
                        value,
                        120,
                        "Email"
                );

        if (!EMAIL.matcher(cleaned)
                .matches()) {

            throw new IllegalArgumentException(
                    "Enter a valid email address"
            );
        }

        return cleaned.toLowerCase();
    }

    public static String phone(
            String value,
            String field) {

        String cleaned =
                required(
                        value,
                        10,
                        field
                );

        if (!PHONE.matcher(cleaned)
                .matches()) {

            throw new IllegalArgumentException(
                    field
                    + " must be a valid "
                    + "10-digit mobile number"
            );
        }

        return cleaned;
    }

    public static int integer(
            String value,
            int min,
            int max,
            String field) {

        try {

            int number =
                    Integer.parseInt(value);

            if (number < min
                    || number > max) {

                throw new IllegalArgumentException(
                        field + " is invalid"
                );
            }

            return number;

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    field + " is invalid"
            );
        }
    }

    public static String oneOf(
            String value,
            String field,
            String... allowed) {

        String cleaned =
                required(
                        value,
                        100,
                        field
                );

        Set<String> values =
                new HashSet<String>(
                        Arrays.asList(allowed)
                );

        if (!values.contains(cleaned)) {

            throw new IllegalArgumentException(
                    "Invalid " + field
            );
        }

        return cleaned;
    }

    public static String appointmentDate(
            String value) {

        LocalDate date =
                LocalDate.parse(
                        required(
                                value,
                                10,
                                "Appointment date"
                        )
                );

        if (date.isBefore(
                LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Past dates cannot be selected"
            );
        }

        return date.toString();
    }

    public static String consultationDateTime(
            String value) {

        LocalDateTime dateTime =
                LocalDateTime.parse(
                        required(
                                value,
                                30,
                                "Consultation date/time"
                        )
                );

        if (dateTime.isBefore(
                LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "Consultation date/time "
                    + "must be in the future"
            );
        }

        return dateTime
                .toString()
                .replace(
                        'T',
                        ' '
                )
                + ":00";
    }

    public static void strongPassword(
            String password) {

        if (password == null
                || password.length() < 8
                || !password.matches(
                        ".*[A-Z].*"
                )
                || !password.matches(
                        ".*[a-z].*"
                )
                || !password.matches(
                        ".*[0-9].*"
                )) {

            throw new IllegalArgumentException(
                    "Password must contain "
                    + "at least 8 characters, "
                    + "uppercase, lowercase "
                    + "and a number"
            );
        }
    }
}