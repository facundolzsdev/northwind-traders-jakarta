package com.northwind.service.factory;

import com.northwind.model.support.Audit;
import com.northwind.model.entity.User;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Factory for creating User entities with required data.
 * Initializes audit information and assigns username, email, and raw password.
 * <p>
 * Note: the password is expected to be already hashed before calling this method.
 * </p>
 */
@ApplicationScoped
public class UserFactory {

    /**
     * Creates a new User with the provided credentials.
     *
     * @param username the username for the account.
     * @param email    the email address of the user.
     * @param password the hashed password (must be pre-hashed).
     * @return a new User entity with audit info initialized.
     */
    public User createUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setAudit(Audit.createDefaultAudit());
        return user;
    }
}
