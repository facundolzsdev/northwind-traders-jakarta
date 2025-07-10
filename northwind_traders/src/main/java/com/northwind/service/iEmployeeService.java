package com.northwind.service;

import com.northwind.model.entity.Employee;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for handling employee-related business logic.
 */
public interface iEmployeeService {

    Optional<Employee> getEmployeeById(Integer id);

    List<Employee> getAllEmployees();

    void saveEmployee(Employee employee);

    void deleteEmployee(Integer id);

    List<Employee> getEmployeeByNameOrUsername(String identifier);

    Optional<Employee> getEmployeeWithUserByUserId(Integer userId);

    Optional<Employee> getByUserId(Integer userId);

}
