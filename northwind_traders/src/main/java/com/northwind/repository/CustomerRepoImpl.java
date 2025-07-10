package com.northwind.repository;

import com.northwind.model.entity.Customer;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

@RequestScoped
public class CustomerRepoImpl implements iCustomerRepository {

    @Inject
    private EntityManager em;

    @Override
    public List<Customer> findAll() {
        return em.createQuery("FROM Customer c", Customer.class).getResultList();
    }

    @Override
    public Customer findById(Integer id) {
        return em.find(Customer.class, id);
    }

    @Override
    public void save(Customer customer) {
        if (customer.getId() != null) {
            em.merge(customer);
        } else {
            em.persist(customer);
        }
    }

    @Override
    public void delete(Integer id) {
        Customer customer = findById(id);
        em.remove(customer);
    }

    @Override
    public Customer findByUserId(Integer userId) {
        try {
            return em.createQuery("SELECT c FROM Customer c WHERE c.user.id = :userId", Customer.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Customer findByIdWithUser(Integer id) {
        try {
            return em.createQuery(
                            "SELECT c FROM Customer c JOIN FETCH c.user WHERE c.id = :id", Customer.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Customer findCustomerWithUserByUserId(Integer userId) {
        try {
            return em.createQuery(
                            "SELECT c FROM Customer c JOIN FETCH c.user WHERE c.user.id = :userId", Customer.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
