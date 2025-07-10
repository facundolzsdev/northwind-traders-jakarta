package com.northwind.service;

import com.northwind.model.entity.Order;
import com.northwind.model.entity.OrderDetail;
import com.northwind.model.enums.OrderArchivedReason;

import java.util.List;

/**
 * Service interface for handling the archival process of completed orders.
 * Defines the contract for converting active orders and their details into historical records.
 */
public interface iHistoricalOrderService {

    // ** SERVICE-LAYER SPECIFIC METHODS ** //

    /**
     * Archives an existing order along with its details into historical records.
     * Stores the original order data, its associated products, and the reason for archiving.
     *
     * @param order   the order to be archived.
     * @param details the list of order details associated with the order.
     * @param reason  the reason why the order is being archived.
     */
    void archiveOrder(Order order, List<OrderDetail> details, OrderArchivedReason reason);

}
