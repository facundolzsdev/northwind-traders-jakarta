package com.northwind.service.factory;

import com.northwind.model.support.Audit;
import com.northwind.model.entity.Employee;
import com.northwind.model.entity.User;

import static com.northwind.util.general.InputSanitizer.*;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Factory for creating Employee entities with proper data sanitization.
 * Ensures all string fields are sanitized before entity creation to prevent injection attacks.
 */
@ApplicationScoped
public class EmployeeFactory {

    /**
     * Creates a new Employee entity from the provided data and associated User.
     * Sanitizes relevant string fields and initializes audit information.
     *
     * @param employee the original employee data to base the new entity on.
     * @param user     the associated User entity for login credentials.
     * @return a new sanitized Employee entity ready for persistence.
     */
    public Employee createEmployee(Employee employee, User user) {
        Employee newEmployee = new Employee();
        newEmployee.setFullName(sanitizeText(employee.getFullName()));
        newEmployee.setDni(employee.getDni());
        newEmployee.setBirthDate(employee.getBirthDate());
        newEmployee.setPhoneNumber(employee.getPhoneNumber());
        newEmployee.setUser(user);
        newEmployee.setAudit(Audit.createDefaultAudit());
        return newEmployee;
    }

}
