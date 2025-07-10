package com.northwind.repository;

import com.northwind.model.entity.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

@RequestScoped
public class UserRepoImpl implements iUserRepository {

    @Inject
    private EntityManager em;

    @Override
    public List<User> findAll() {
        return em.createQuery("FROM User u", User.class).getResultList();
    }

    @Override
    public User findById(Integer id) {
        return em.find(User.class, id);
    }

    @Override
    public void save(User user) {
        if (user.getId() != null) {
            em.merge(user);
        } else {
            em.persist(user);
        }
    }

    @Override
    public void delete(Integer id) {
        User user = findById(id);
        em.remove(user);
    }

    @Override
    public User findUserByUsernameOrEmail(String identifier) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :identifier", User.class)
                    .setParameter("identifier", identifier)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean usernameExists(String username) {
        Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public boolean emailExists(String email) {
        Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }

}
