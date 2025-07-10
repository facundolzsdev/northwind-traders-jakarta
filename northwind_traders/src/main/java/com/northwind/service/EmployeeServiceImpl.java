package com.northwind.service;

import com.northwind.exceptions.EmployeeServiceException;
import com.northwind.model.entity.Employee;
import com.northwind.repository.iEmployeeRepository;
import com.northwind.util.exception.ServiceExceptionUtil;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class EmployeeServiceImpl implements iEmployeeService {

    @Inject
    private iEmployeeRepository employeeRepo;
    @Inject
    private Logger logger;

    @Override
    public Optional<Employee> getEmployeeById(Integer id) {
        return Optional.ofNullable(employeeRepo.findById(id));
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    @Override
    public void saveEmployee(Employee employee) {
        try {
            employeeRepo.save(employee);
            logger.log(Level.INFO, "Employee saved with ID: {0}", employee.getId());
        } catch (Exception e) {
            handleException("saving employee: " + employee.getFullName(), e, Level.SEVERE);
        }
    }

    @Override
    public void deleteEmployee(Integer id) {
        try {
            Employee employee = employeeRepo.findById(id);
            if (employee == null) {
                throw new EmployeeServiceException("Employee not found.");
            }

            employeeRepo.delete(id);
            logger.log(Level.INFO, "Employee deleted with ID: {0}", id);
        } catch (Exception e) {
            handleException("deleting employee with ID: " + id, e, Level.SEVERE);
        }
    }

    @Override
    public Optional<Employee> getByUserId(Integer userId) {
        return Optional.ofNullable(employeeRepo.findByUserId(userId));
    }

    @Override
    public List<Employee> getEmployeeByNameOrUsername(String identifier) {
        return employeeRepo.findEmployeeByNameOrUsername(identifier);
    }

    @Override
    public Optional<Employee> getEmployeeWithUserByUserId(Integer userId) {
        return Optional.ofNullable(employeeRepo.findEmployeeWithUserByUserId(userId));
    }

    private void handleException(String action, Exception e, Level level) {
        ServiceExceptionUtil.handle(logger, action, e, level, EmployeeServiceException.class, null);
    }
}
