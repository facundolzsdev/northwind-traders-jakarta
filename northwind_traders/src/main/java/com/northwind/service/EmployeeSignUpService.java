package com.northwind.service;

import com.northwind.exceptions.SignUpServiceException;
import com.northwind.model.entity.Employee;
import com.northwind.model.entity.User;
import com.northwind.model.enums.RoleType;
import com.northwind.service.factory.EmployeeFactory;
import com.northwind.service.factory.UserFactory;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class EmployeeSignUpService {

    @Inject
    private iUserService userService;
    @Inject
    private iEmployeeService employeeService;
    @Inject
    private UserFactory userFactory;
    @Inject
    private EmployeeFactory employeeFactory;
    @Inject
    private Logger logger;

    public void registerEmployee(String username, String email, String password, Employee employee) {
        try {
            User user = userFactory.createUser(username, email, password);
            userService.registerUser(user, RoleType.EMPLOYEE);

            Employee newEmployee = employeeFactory.createEmployee(employee, user);
            employeeService.saveEmployee(newEmployee);

            logger.log(Level.INFO, "Employee registered successfully: {0}", user.getUsername());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during Employee registration: " + username, e);
            throw new SignUpServiceException("An unexpected error occurred during Employee registration.", e);
        }
    }

}
