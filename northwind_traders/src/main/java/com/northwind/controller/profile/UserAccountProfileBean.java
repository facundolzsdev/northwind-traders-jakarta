package com.northwind.controller.profile;

import com.northwind.context.AuthenticatedUserContext;
import com.northwind.controller.base.BaseProfileBean;
import com.northwind.exceptions.UserServiceException;
import com.northwind.model.entity.User;
import com.northwind.model.dto.UserAccountProfileDTO;
import com.northwind.service.iUserService;
import com.northwind.util.jsf.NavigationUtil;
import com.northwind.util.security.PasswordEncoder;
import com.northwind.util.validation.AccountInfoValidator;
import jakarta.faces.FacesException;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.*;

import java.util.logging.Logger;

/**
 * Managed bean responsible for managing the profile of an user,
 * including loading and updating personal information.
 * <p>
 * Extends {@link BaseProfileBean} to reuse common profile operations.
 * </p>
 */
@Getter
@Setter
@Named("accountProfile")
@ViewScoped
public class UserAccountProfileBean extends BaseProfileBean<User, UserAccountProfileDTO> {

    @Inject
    private iUserService userService;
    @Inject
    private AuthenticatedUserContext authUserContext;
    @Inject
    private AccountInfoValidator validator;
    @Inject
    private Logger logger;

    @Override
    protected boolean validate() {
        return validator.validateAccountProfile(dto);
    }

    @Override
    protected void persist(User user) throws UserServiceException {
        boolean passwordChanged = hasPasswordChanged();
        applyChanges(dto, user);
        if (passwordChanged) {
            user.setPassword(dto.getPassword());
        }
        userService.updateUser(user, passwordChanged);
    }

    @Override
    protected Integer getEntityId() {
        return originalEntity != null ? originalEntity.getId() : null;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected void loadEntity() {
        Integer idToLoad = resolveUserId();

        userService.getUserById(idToLoad)
                .ifPresentOrElse(this::initializeUserData, ()
                        -> handleLoadFailure("User not found for ID: " + idToLoad));
    }

    @Override
    protected void applyChanges(UserAccountProfileDTO dto, User user) {
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
    }

    @Override
    protected UserAccountProfileDTO mapToDTO(User user) {
        return UserAccountProfileDTO.builder()
                .username(user.getUsername())
                .originalUsername(user.getUsername())
                .email(user.getEmail())
                .originalEmail(user.getEmail())
                .password(null)
                .confirmPassword(null)
                .build();
    }

    private void initializeUserData(User user) {
        this.originalEntity = user;
        this.dto = mapToDTO(user);
    }

    /**
     * Resolves the user ID to load the profile for. If the 'userId' request parameter is present,
     * it attempts to parse and return it. Otherwise, returns the ID of the currently authenticated user.
     * Redirects to the error page and throws a FacesException if the parameter is invalid.
     *
     * @return the resolved user ID
     * @throws FacesException if the 'userId' parameter is not a valid integer
     */
    private Integer resolveUserId() {
        String userIdParam = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap()
                .get("userId");

        if (userIdParam == null) {
            return authUserContext.getUserId();
        }

        try {
            return Integer.valueOf(userIdParam);
        } catch (NumberFormatException e) {
            logger.severe("Invalid userId parameter: " + userIdParam);
            NavigationUtil.redirectToErrorPage();
            throw new FacesException("Invalid userId parameter", e);
        }
    }

    protected boolean hasChanges() {
        return hasEmailChanged() || hasUsernameChanged() || hasPasswordChanged();
    }

    private boolean hasPasswordChanged() {
        return dto.getPassword() != null &&
                !dto.getPassword().isEmpty() &&
                !PasswordEncoder.verify(dto.getPassword(), originalEntity.getPassword());
    }

    private boolean hasEmailChanged() {
        return !dto.getEmail().equals(dto.getOriginalEmail());
    }

    private boolean hasUsernameChanged() {
        return !dto.getUsername().equals(dto.getOriginalUsername());
    }

}
