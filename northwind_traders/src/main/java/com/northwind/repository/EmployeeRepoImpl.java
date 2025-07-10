package com.northwind.repository;

import com.northwind.model.entity.Employee;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

@RequestScoped
public class EmployeeRepoImpl implements iEmployeeRepository {

    @Inject
    private EntityManager em;

    @Override
    public List<Employee> findAll() {
        return em.createQuery("FROM Employee e", Employee.class).getResultList();
    }

    @Override
    public Employee findById(Integer id) {
        return em.find(Employee.class, id);
    }

    @Override
    public void save(Employee employee) {
        if (employee.getId() != null) {
            em.merge(employee);
        } else {
            em.persist(employee);
        }
    }

    @Override
    public void delete(Integer id) {
        Employee employee = findById(id);
        em.remove(employee);
    }

    @Override
    public Employee findEmployeeWithUserByUserId(Integer userId) {
        try {
            return em.createQuery(
                            "SELECT e FROM Employee e JOIN FETCH e.user WHERE e.user.id = :userId", Employee.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Employee findByUserId(Integer userId) {
        try {
            return em.createQuery("SELECT e FROM Employee e WHERE e.user.id = :userId", Employee.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    @Override
    public List<Employee> findEmployeeByNameOrUsername(String identifier) {
        return em.createQuery("SELECT e FROM Employee e LEFT OUTER JOIN FETCH e.user" +
                        " WHERE LOWER(e.fullName) LIKE :identifier OR e.user.username LIKE :identifier", Employee.class)
                .setParameter("identifier", "%" + identifier + "%")
                .getResultList();
    }
}
