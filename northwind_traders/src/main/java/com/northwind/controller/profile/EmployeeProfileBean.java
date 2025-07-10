package com.northwind.controller.profile;

import com.northwind.controller.base.BaseProfileBean;
import com.northwind.exceptions.EmployeeServiceException;
import com.northwind.model.entity.Employee;
import com.northwind.model.dto.EmployeeProfileDTO;
import com.northwind.service.iEmployeeService;
import com.northwind.util.validation.PersonalInfoValidator;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.util.logging.Logger;

/**
 * Managed bean responsible for managing the profile of an employee,
 * including loading and updating personal information.
 * <p>
 * Extends {@link BaseProfileBean} to reuse common profile operations.
 */
@Getter
@Setter
@Named("employeeProfile")
@ViewScoped
public class EmployeeProfileBean extends BaseProfileBean<Employee, EmployeeProfileDTO> {

    private Integer requestedEmployeeId;

    @Inject
    private iEmployeeService employeeService;
    @Inject
    private PersonalInfoValidator validator;
    @Inject
    private Logger logger;

    @Override
    protected boolean validate() {
        return validator.validateEmployeeProfile(dto);
    }

    @Override
    protected void persist(Employee employee) throws EmployeeServiceException {
        employeeService.saveEmployee(employee);
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
        Integer id = resolveEmployeeId();
        if (id == null) {
            handleLoadFailure("Invalid employee ID (null) while trying to load profile");
            return;
        }

        this.requestedEmployeeId = id;

        employeeService.getEmployeeById(id)
                .ifPresentOrElse(e -> {
                    this.originalEntity = e;
                    this.dto = mapToDTO(e);
                }, () -> handleLoadFailure("Failed to load employee with ID: " + requestedEmployeeId));
    }

    @Override
    protected void applyChanges(EmployeeProfileDTO dto, Employee employee) {
        employee.setFullName(dto.getFullName());
        employee.setDni(dto.getDni());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setBirthDate(dto.getBirthDate());
    }

    @Override
    protected EmployeeProfileDTO mapToDTO(Employee employee) {
        return EmployeeProfileDTO.builder()
                .fullName(employee.getFullName())
                .dni(employee.getDni())
                .phoneNumber(employee.getPhoneNumber())
                .birthDate(employee.getBirthDate())
                .build();
    }

    /**
     * Extracts and validates the "employeeId" request parameter.
     *
     * @return the parsed employee ID or null if invalid or missing
     */
    private Integer resolveEmployeeId() {
        String param = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap()
                .get("employeeId");

        if (param == null) {
            logger.warning("Missing employeeId parameter in request");
            return null;
        }

        try {
            return Integer.valueOf(param);
        } catch (NumberFormatException e) {
            logger.warning("Invalid employeeId parameter: " + param);
            return null;
        }
    }

}
