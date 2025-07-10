package com.northwind.repository;

import com.northwind.model.entity.Admin;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

@RequestScoped
public class AdminRepoImpl implements iAdminRepository {

    @Inject
    private EntityManager em;

    @Override
    public List<Admin> findAll() {
        return em.createQuery("FROM Admin a", Admin.class).getResultList();
    }

    @Override
    public Admin findById(Integer id) {
        return em.find(Admin.class, id);
    }

    @Override
    public void save(Admin admin) {
        if (admin.getId() != null) {
            em.merge(admin);
        } else {
            em.persist(admin);
        }
    }

    @Override
    public void delete(Integer id) {
        Admin admin = findById(id);
        em.remove(admin);
    }

    @Override
    public Admin findAdminWithUserByUserId(Integer userId) {
        try {
            return em.createQuery(
                            "SELECT a FROM Admin a JOIN FETCH a.user WHERE a.user.id = :userId", Admin.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Admin findByIdWithUser(Integer id) {
        try {
            return em.createQuery(
                            "SELECT a FROM Admin a JOIN FETCH a.user WHERE a.id = :id", Admin.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}

