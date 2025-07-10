package com.northwind.util.validation;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

/**
 * Utility class providing predefined {@link Pattern} instances
 * for common user and personal data validation.
 */
@UtilityClass
public class ValidationPatterns {

    public static final Pattern EMAIL =
            Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    public static final Pattern PASSWORD =
            Pattern.compile("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$");
    public static final Pattern USERNAME =
            Pattern.compile("^[a-z0-9_]{5,12}$");
    public static final Pattern PHONE =
            Pattern.compile("^\\+?[0-9]{1,3}[\\s-]?\\(?[0-9]{1,4}\\)?[\\s-]?[0-9]{3}[\\s-]?[0-9]{4}$");
    public static final Pattern POSTAL_CODE =
            Pattern.compile("^[a-zA-Z0-9\\s-]{3,10}$");
    public static final Pattern DNI =
            Pattern.compile("^[0-9]{8}$");
}