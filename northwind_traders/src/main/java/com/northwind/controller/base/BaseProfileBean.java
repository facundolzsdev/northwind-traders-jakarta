package com.northwind.controller.base;

import com.northwind.util.jsf.GrowlUtil;
import com.northwind.util.jsf.NavigationUtil;
import com.northwind.util.constants.messages.DataUpdateMessages;
import com.northwind.util.constants.messages.GrowlTitles;
import jakarta.annotation.PostConstruct;
import lombok.*;

import static com.northwind.util.exception.BeanExceptionUtil.*;

import java.io.Serializable;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract base bean for managing the profile of an entity in a JSF application.
 * <p>
 * Provides a template for common profile operations such as loading, editing,
 * validating, and persisting user-related data. Designed to be extended by
 * concrete managed beans that work with specific entity/DTO types.
 *
 * @param <T> the entity type (e.g., User, Customer)
 * @param <D> the DTO type used to transfer data to/from the UI
 */
@Getter
@Setter
public abstract class BaseProfileBean<T, D> implements Serializable {

    protected D dto;
    protected T originalEntity;

    /**
     * Provides the logger instance to be used for logging errors or events.
     *
     * @return the {@link Logger} for this bean
     */
    protected abstract Logger getLogger();

    /**
     * Validates the DTO before applying changes.
     *
     * @return true if validation passes; false otherwise
     */
    protected abstract boolean validate();

    protected abstract void applyChanges(D dto, T entity);

    /**
     * Maps the given entity to its corresponding DTO representation.
     *
     * @param entity the entity to map
     * @return the DTO populated from the entity
     */
    protected abstract D mapToDTO(T entity);

    protected abstract void persist(T entity) throws Exception;

    protected abstract Integer getEntityId();

    /**
     * Handles the failure when loading the entity, e.g., not found or access denied.
     */
    protected void handleLoadFailure(String logMessage) {
        getLogger().severe(logMessage);
        NavigationUtil.redirectToErrorPage();
    }

    /**
     * Initializes the bean after construction by attempting to load the entity.
     * Redirects to the error page if any exception occurs during loading.
     */
    @PostConstruct
    public void init() {
        try {
            loadEntity();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to load entity for profile", e);
            NavigationUtil.redirectToErrorPage();
        }
    }

    /**
     * Applies updates to the entity if changes are detected and validation passes.
     * Displays growl messages for success, failure, or when no changes are made.
     */
    public void update() {
        if (!hasChanges()) {
            GrowlUtil.warning(GrowlTitles.INFO_MESSAGE, DataUpdateMessages.NO_DATA_CHANGES_DETECTED);
            return;
        }

        if (!validate()) {
            return;
        }

        try {
            applyChanges(dto, originalEntity);
            persist(originalEntity);
            dto = mapToDTO(originalEntity);
            GrowlUtil.success(GrowlTitles.SUCCESS_OPERATION_COMPLETED, DataUpdateMessages.DATA_UPDATE_SUCCESS);
        } catch (Exception e) {
            handle(getLogger(), "updating entity ID: " + getEntityId(), e,
                    DataUpdateMessages.DATA_UPDATE_FAILURE, Level.SEVERE);
        }
    }

    /**
     * Cancels the current edit by resetting the DTO to reflect the original entity's state.
     */
    public void cancelEdit() {
        dto = mapToDTO(originalEntity);
    }

    /**
     * Checks whether any changes have been made to the DTO compared to the original entity.
     *
     * @return true if there are changes, false otherwise
     */
    protected boolean hasChanges() {
        return !mapToDTO(originalEntity).equals(dto);
    }

    protected abstract void loadEntity();

}

