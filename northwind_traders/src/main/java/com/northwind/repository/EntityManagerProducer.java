package com.northwind.repository;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@RequestScoped
public class EntityManagerProducer {

    @PersistenceContext(name = "northwindTradersPU")
    private EntityManager em;

    @Produces
    @RequestScoped
    private EntityManager beanEntityManager() {
        return em;
    }
}
