package com.northwind.util.validation;

import com.northwind.model.enums.RoleType;
import lombok.experimental.UtilityClass;

import static com.northwind.util.constants.ViewPaths.*;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Utility class responsible for validating access to routes
 * based on role permissions and public paths.
 */
@UtilityClass
public class RouteValidator {

    private static final Pattern STATIC_RESOURCES_PATTERN = Pattern.compile("/.+\\.(css|js|png|jpg|gif|ico|woff2?|ttf)");
    private static final Pattern FACES_RESOURCE_PATTERN = Pattern.compile("/jakarta.faces.resource/.*");
    private static final Pattern RESOURCES_FOLDER_PATTERN = Pattern.compile("/resources/.*");

    private static final List<Pattern> PUBLIC_PATTERNS = List.of(
            jsfPagePattern(Common.LOGIN),
            jsfPagePattern(Common.ERROR_CUSTOM),
            jsfPagePattern(Common.ERROR_ACCESS_DENIED),
            jsfPagePattern(Common.CUSTOMER_SIGNUP),
            STATIC_RESOURCES_PATTERN,
            FACES_RESOURCE_PATTERN,
            RESOURCES_FOLDER_PATTERN
    );

    /**
     * Map containing patterns for routes accessible by each user role.
     * Also enforces restrictions (e.g., employees can't access customer signup).
     */
    private static final Map<RoleType, List<Pattern>> ROLE_ACCESS_MAP = Map.of(
            RoleType.ADMIN, List.of(
                    Pattern.compile("/views/admin/.*"),
                    Pattern.compile(Common.USER_ACCOUNT_EDIT.replace(".jsf", "\\.jsf"))
            ),
            RoleType.EMPLOYEE, List.of(
                    Pattern.compile("/views/employee/.*")
            ),
            RoleType.CUSTOMER, List.of(
                    Pattern.compile("/views/customer/.*"),
                    Pattern.compile(Common.USER_ACCOUNT_EDIT.replace(".jsf", "\\.jsf")),
                    Pattern.compile(Common.CUSTOMER_SIGNUP.replace(".jsf", "\\.jsf"))
            )
    );

    public static boolean isPublicPath(String path) {
        return PUBLIC_PATTERNS.stream().anyMatch(p -> p.matcher(path).matches());
    }

    /**
     * Checks if a user with the given role has access to the specified path.
     *
     * @param role The role of the current user.
     * @param path The requested URL path.
     * @return true if access is allowed, false otherwise.
     */
    public static boolean hasAccess(RoleType role, String path) {
        if (isPublicPath(path)) {
            return true;
        }

        if (role.isEmployee() && jsfPagePattern(Common.USER_ACCOUNT_EDIT).matcher(path).matches()) {
            return false;
        }

        return ROLE_ACCESS_MAP.getOrDefault(role, List.of())
                .stream()
                .anyMatch(p -> p.matcher(path).matches());
    }

    private static Pattern jsfPagePattern(String path) {
        return Pattern.compile(path.replace(".jsf", "\\.jsf"));
    }
}
