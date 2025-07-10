package com.northwind.service;

import com.northwind.exceptions.UserServiceException;
import com.northwind.model.entity.Role;
import com.northwind.model.entity.User;
import com.northwind.model.enums.RoleType;
import com.northwind.repository.iUserRepository;
import com.northwind.util.security.PasswordEncoder;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import static com.northwind.util.exception.ServiceExceptionUtil.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class UserServiceImpl implements iUserService {

    @Inject
    private iUserRepository userRepo;
    @Inject
    private iRoleService roleService;
    @Inject
    private Logger logger;

    @Override
    public Optional<User> getUserById(Integer id) {
        return Optional.ofNullable(userRepo.findById(id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public void saveUser(User user) {
        try {
            userRepo.save(user);
            logger.log(Level.INFO, "User saved with ID: {0}", user.getId());
        } catch (Exception e) {
            handleException("saving user: " + user.getUsername(), e, Level.SEVERE);
        }
    }

    @Override
    public void deleteUser(Integer id) {
        try {
            User user = findUserByIdOrThrow(id, "Cannot delete user. User not found.");
            userRepo.delete(id);
            logger.log(Level.INFO, "User deleted with ID: {0}", id);
        } catch (Exception e) {
            handleException("deleting user with ID: " + id, e, Level.SEVERE);
        }
    }

    @Override
    public Optional<User> getUserByUsernameOrEmail(String identifier) {
        return Optional.ofNullable(userRepo.findUserByUsernameOrEmail(identifier));
    }

    @Override
    public void registerUser(User user, RoleType roleType) {
        try {
            validateUniqueUsernameOrEmail(user.getUsername());
            user.setRole(getRoleByTypeOrThrow(roleType));

            hashPassword(user);

            userRepo.save(user);
            logger.log(Level.INFO, "User registered successfully: {0}", user.getUsername());
        } catch (UserServiceException e) {
            logger.log(Level.WARNING, "Error in user registration: {0}", e.getMessage());
            throw e;
        } catch (Exception e) {
            handleException("registering user: " + user.getUsername(), e, Level.SEVERE);
        }
    }

    @Override
    public boolean usernameExists(String username) {
        return userRepo.usernameExists(username);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepo.emailExists(email);
    }

    /**
     * Updates an existing user, handling optional password hashing.
     * <p>
     * Note: this method was created separately from {@code saveUser(User)} to explicitly handle cases
     * where the password may or may not have been changed.
     * This avoids unnecessary re-hashing of an already hashed password.
     * </p>
     *
     * @param user            the user entity to update
     * @param passwordChanged {@code true} if the password has been modified and should be re-hashed;
     *                        {@code false} otherwise
     */
    @Override
    public void updateUser(User user, boolean passwordChanged) {
        try {
            hashPasswordIfNecessary(user, passwordChanged);
            userRepo.save(user);
            logger.log(Level.INFO, "User updated with ID: {0}", user.getId());
        } catch (Exception e) {
            handleException("updating user: " + user.getUsername(), e, Level.SEVERE);
        }
    }

    private void validateUniqueUsernameOrEmail(String username) {
        if (userRepo.findUserByUsernameOrEmail(username) != null) {
            throw new UserServiceException("Username or email address already registered.");
        }
    }

    private Role getRoleByTypeOrThrow(RoleType roleType) {
        return roleService.getRoleByType(roleType)
                .orElseThrow(() -> new UserServiceException("The specified role does not exist."));
    }

    private User findUserByIdOrThrow(Integer id, String errorMessage) {
        User user = userRepo.findById(id);
        if (user == null) {
            handleException(errorMessage + " User ID: " + id, new UserServiceException(errorMessage), Level.WARNING);
        }
        return user;
    }

    private void hashPasswordIfNecessary(User user, boolean passwordChanged) {
        if (passwordChanged) {
            hashPassword(user);
        }
    }

    private void hashPassword(User user) {
        user.setPassword(PasswordEncoder.hash(user.getPassword()));
    }

    private void handleException(String action, Exception e, Level level) {
        handle(logger, action, e, level, UserServiceException.class, null);
    }
}

