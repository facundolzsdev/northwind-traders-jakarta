package com.northwind.service.factory;

import com.northwind.model.entity.HistoricalOrder;
import com.northwind.model.entity.HistoricalOrderDetails;
import com.northwind.model.entity.Order;
import com.northwind.model.entity.OrderDetail;
import com.northwind.model.enums.OrderArchivedReason;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Factory for creating HistoricalOrder entities.
 * Converts an active Order and its OrderDetails into a historical record,
 * including archiving metadata and associated detail entries.
 */
@ApplicationScoped
public class HistoricalOrderFactory {

    /**
     * Creates a HistoricalOrder entity from the given Order and its details.
     *
     * @param order   the original Order entity to archive.
     * @param details the list of associated OrderDetail entities.
     * @param reason  the reason for archiving.
     * @return a fully prepared HistoricalOrder entity with its details.
     */
    public HistoricalOrder createFrom(Order order, List<OrderDetail> details, OrderArchivedReason reason) {
        HistoricalOrder historicalOrder = buildHistoricalOrder(order, reason);
        addHistoricalDetails(historicalOrder, details);
        return historicalOrder;
    }

    private HistoricalOrder buildHistoricalOrder(Order order, OrderArchivedReason reason) {
        HistoricalOrder historicalOrder = new HistoricalOrder();
        historicalOrder.setOrderID(order.getId());
        historicalOrder.setCustomerFullName(order.getCustomer().getFullName());
        historicalOrder.setEmployeeID(extractEmployeeId(order));
        historicalOrder.setOrderDate(order.getOrderDate());
        historicalOrder.setStatus(order.getStatus());
        historicalOrder.setTotalAmount(order.getTotalAmount());
        historicalOrder.setArchivedDate(LocalDateTime.now());
        historicalOrder.setArchivedReason(reason);

        return historicalOrder;
    }

    private void addHistoricalDetails(HistoricalOrder historicalOrder, List<OrderDetail> details) {
        for (OrderDetail detail : details) {
            HistoricalOrderDetails historicalDetail = new HistoricalOrderDetails();
            historicalDetail.setProduct(detail.getProduct());
            historicalDetail.setProductQuantity(detail.getProductQuantity());
            historicalDetail.setProductPrice(detail.getProductPrice());
            historicalDetail.setHistoricalOrder(historicalOrder);

            historicalOrder.getHistoricalOrderDetails().add(historicalDetail);
        }
    }

    private Integer extractEmployeeId(Order order) {
        return order.getEmployee() != null ? order.getEmployee().getId() : null;
    }
}