package com.northwind.service;

import com.northwind.exceptions.HistoricalOrderServiceException;
import com.northwind.model.entity.HistoricalOrder;
import com.northwind.model.entity.Order;
import com.northwind.model.entity.OrderDetail;
import com.northwind.model.enums.OrderArchivedReason;
import com.northwind.repository.iHistoricalOrderRepository;
import com.northwind.service.factory.HistoricalOrderFactory;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.logging.Logger;

@Stateless
public class HistoricalOrderServiceImpl implements iHistoricalOrderService {

    @Inject
    private EntityManager em;
    @Inject
    private iHistoricalOrderRepository historicalOrderRepo;
    @Inject
    private HistoricalOrderFactory historicalOrderFactory;
    @Inject
    private Logger logger;

    @Override
    public void archiveOrder(Order order, List<OrderDetail> details, OrderArchivedReason reason) {
        try {
            HistoricalOrder historicalOrder = historicalOrderFactory.createFrom(order, details, reason);
            em.persist(historicalOrder);
        } catch (Exception e) {
            logger.severe("Error archiving order ID: " + order.getId() +
                    " - Reason: " + reason + " - " + e.getMessage());
            throw new HistoricalOrderServiceException("Could not archive order ID " + order.getId(), e);
        }
    }

}
