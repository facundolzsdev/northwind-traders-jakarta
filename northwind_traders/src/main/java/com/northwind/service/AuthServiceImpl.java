package com.northwind.service;

import com.northwind.exceptions.AuthServiceException;
import com.northwind.exceptions.InactiveEmployeeException;
import com.northwind.model.entity.Employee;
import com.northwind.model.entity.User;
import com.northwind.util.security.PasswordEncoder;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@Stateless
public class AuthServiceImpl implements iAuthService {

    @Inject
    private iUserService userService;
    @Inject
    private iEmployeeService employeeService;
    @Inject
    private Logger logger;

    @Override
    public Optional<User> login(String identifier, String password) throws AuthServiceException {
        Optional<User> userOpt = userService.getUserByUsernameOrEmail(identifier);

        if (!isValidUser(userOpt, password)) {
            return Optional.empty(); // Note: will throw InvalidCredentialsException in Login Bean.
        }

        User user = userOpt.get();

        if (user.getRole().getName().isEmployee()) {
            validateEmployeeStatus(user);
        }

        return Optional.of(user);
    }

    /**
     * Verifies that the user exists and that the provided raw password matches
     * the hashed password stored in the database using a password encoder.
     *
     * @param userOpt     Optional containing the user retrieved from the database
     * @param rawPassword The plaintext password entered by the user attempting to log in
     * @return {@code true} if the user exists and the password is valid; {@code false} otherwise
     */
    private boolean isValidUser(Optional<User> userOpt, String rawPassword) {
        return userOpt.isPresent() &&
                PasswordEncoder.verify(rawPassword, userOpt.get().getPassword());
    }

    /**
     * Checks if employee user is active
     *
     * @param user Authenticated user
     * @throws InactiveEmployeeException if employee is inactive
     */
    private void validateEmployeeStatus(User user) throws InactiveEmployeeException {
        if (user.getRole().getName().isEmployee() && !isEmployeeActive(user)) {
            throw new InactiveEmployeeException();
        }
    }

    /**
     * Checks employee active status
     *
     * @param user User to check
     * @return true if employee is active
     */
    private boolean isEmployeeActive(User user) {
        return employeeService.getByUserId(user.getId())
                .map(Employee::isActive)
                .orElse(false);
    }

}