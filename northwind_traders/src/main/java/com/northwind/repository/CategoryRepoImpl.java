package com.northwind.repository;

import com.northwind.model.entity.Category;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

@RequestScoped
public class CategoryRepoImpl implements iCrudRepository<Category, Byte> {

    @Inject
    private EntityManager em;

    @Override
    public List<Category> findAll() {
        return em.createQuery("FROM Category", Category.class).getResultList();
    }

    @Override
    public Category findById(Byte id) {
        return em.find(Category.class, id);
    }

    @Override
    public void save(Category category) {
        if (category.getId() != null) {
            em.merge(category);
        } else {
            em.persist(category);
        }
    }

    @Override
    public void delete(Byte id) {
        Category category = em.find(Category.class, id);
        if (category != null) {
            em.remove(category);
        } else {
            throw new IllegalArgumentException("Category cannot be null.");
        }
    }
}
