package com.northwind.controller.admin;

import com.northwind.exceptions.EmployeeServiceException;
import com.northwind.model.entity.Employee;
import com.northwind.service.iEmployeeService;
import com.northwind.util.jsf.GrowlUtil;

import static com.northwind.util.constants.messages.GrowlTitles.*;

import com.northwind.util.constants.messages.AdminMessages;
import com.northwind.util.constants.messages.DataUpdateMessages;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Managed Bean for managing employee data in the Admin view.
 * <p>
 * Handles employee search, listing, and active status toggling.
 * It is initialized with the full list of employees and allows filtering by name or username.
 * </p>
 */
@Getter
@Setter
@Named("employeeMgmtBean")
@ViewScoped
public class EmployeeManagementBean implements Serializable {

    private String searchQuery;
    private List<Employee> employees;
    private Employee selectedEmployee;

    @Inject
    private iEmployeeService employeeService;
    @Inject
    private Logger logger;

    @PostConstruct
    public void init() {
        reloadTable();
    }

    /**
     * Filters the employee list based on the search query.
     * This method uses the employee service to fetch employees matching the query string.
     */
    public void find() {
        this.employees = employeeService.getEmployeeByNameOrUsername(searchQuery);
    }

    /**
     * Toggles the active status of the selected employee and persists the change.
     * Displays a growl message indicating the result of the operation.
     */
    public void toggleActiveStatus() {
        if (selectedEmployee == null) return;

        try {
            selectedEmployee.setActive(!selectedEmployee.isActive());
            employeeService.saveEmployee(selectedEmployee);
            reloadTable();

            String fullName = selectedEmployee.getFullName();
            String status = selectedEmployee.isActive() ? "habilitado" : "inhabilitado";
            String message = String.format(AdminMessages.EMPLOYEE_STATUS_TOGGLE_SUCCESS, fullName, status);

            GrowlUtil.success(SUCCESS_OPERATION_COMPLETED, message);
        } catch (EmployeeServiceException e) {
            logger.log(Level.SEVERE, "Employee status change error", e);
            GrowlUtil.error(ERROR_OPERATION_FAILED, DataUpdateMessages.DATA_UPDATE_FAILURE);
        }
    }

    public void reloadTable() {
        searchQuery = null;
        employees = employeeService.getAllEmployees();
    }

    public void prepareToggleActive(Employee employee) {
        this.selectedEmployee = employee;
    }

}
