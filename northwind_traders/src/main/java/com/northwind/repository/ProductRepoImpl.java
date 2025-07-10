package com.northwind.repository;

import com.northwind.model.entity.Product;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

@RequestScoped
public class ProductRepoImpl implements iProductRepository {

    @Inject
    private EntityManager em;

    @Override
    public List<Product> findAll() {
        return em.createQuery("SELECT p FROM Product p LEFT OUTER JOIN FETCH p.category", Product.class)
                .getResultList();
    }

    @Override
    public Product findById(Integer id) {
        try {
            return em.createQuery("SELECT p FROM Product p LEFT OUTER JOIN FETCH p.category WHERE p.id=:id", Product.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void save(Product product) {
        if (product.getId() != null) {
            em.merge(product);
        } else {
            em.persist(product);
        }
    }

    @Override
    public void delete(Integer id) {
        Product product = findById(id);
        em.remove(product);
    }

    @Override
    public List<Product> findProductByName(String name) {
        return em.createQuery("SELECT p FROM Product p LEFT OUTER JOIN FETCH p.category" +
                        " WHERE LOWER(p.name) LIKE :name", Product.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }
}
