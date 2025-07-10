package com.northwind.repository;

import com.northwind.model.entity.Employee;

import java.util.List;

/**
 * Repository interface for performing database operations related to {@link Employee}.
 * <p>
 * Extends {@code iCrudRepository} to provide standard CRUD functionality.
 * </p>
 */
public interface iEmployeeRepository extends iCrudRepository<Employee, Integer> {

    List<Employee> findEmployeeByNameOrUsername(String identifier);

    /**
     * Finds an employee by their associated user's ID, including the user entity.
     *
     * @param userId the ID of the associated user
     * @return the employee with its associated user, or null if not found
     */
    Employee findEmployeeWithUserByUserId(Integer userId);

    /**
     * Finds a employee by the associated user ID.
     *
     * @param userId the ID of the user linked to the employee
     * @return the employee associated with the given user ID
     */
    Employee findByUserId(Integer userId);

}
