package com.northwind.repository;

import com.northwind.model.entity.HistoricalOrder;

/**
 * Repository interface for managing historical orders stored in the system.
 * <p>
 * Extends {@code iCrudRepository} to provide basic CRUD operations for {@link HistoricalOrder} entities.
 * </p>
 */
public interface iHistoricalOrderRepository extends iCrudRepository<HistoricalOrder, Integer> {
}
