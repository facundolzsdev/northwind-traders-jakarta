package com.northwind.repository;

import com.northwind.model.entity.HistoricalOrder;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

@RequestScoped
public class HistoricalOrderRepoImpl implements iHistoricalOrderRepository {

    @Inject
    private EntityManager em;

    @Override
    public List<HistoricalOrder> findAll() {
        return null;
    }

    @Override
    public HistoricalOrder findById(Integer integer) {
        return null;
    }

    @Override
    public void save(HistoricalOrder historicalOrder) {
    }

    @Override
    public void delete(Integer integer) {
    }

}
