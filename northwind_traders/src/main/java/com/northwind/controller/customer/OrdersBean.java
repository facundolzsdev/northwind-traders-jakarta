package com.northwind.controller.customer;

import com.northwind.context.AuthenticatedUserContext;
import com.northwind.context.CustomerContext;
import com.northwind.exceptions.AuthServiceException;
import com.northwind.exceptions.OrderServiceException;
import com.northwind.exceptions.ValidationException;
import com.northwind.model.entity.Order;
import com.northwind.model.entity.OrderDetail;
import com.northwind.model.enums.OrderArchivedReason;
import com.northwind.service.iHistoricalOrderService;
import com.northwind.service.iOrderService;

import static com.northwind.util.exception.BeanExceptionUtil.handle;
import static com.northwind.util.validation.OrderReviewValidator.*;

import com.northwind.util.constants.messages.GrowlTitles;
import com.northwind.util.constants.messages.OrderMessages;
import com.northwind.util.general.OrderStatusUtil;
import com.northwind.util.jsf.GrowlUtil;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Managed bean responsible for handling customer order-related actions
 * in the view layer. It interacts with the order service to retrieve,
 * display, and manage customer orders, including order cancellation and detail visualization.
 */
@Getter
@Setter
@Named
@ViewScoped
public class OrdersBean implements Serializable {

    private List<Order> orders;
    private Order selectedOrder;
    private List<OrderDetail> selectedOrderDetails;

    @Inject
    private iOrderService orderService;
    @Inject
    private iHistoricalOrderService historicalOrderService;

    @Inject
    private AuthenticatedUserContext authUserContext;
    @Inject
    private CustomerContext customerContext;

    @Inject
    private Logger logger;

    /**
     * Initializes the bean after construction by attempting to load
     * all orders for the currently authenticated customer.
     * If the user is not authenticated as a customer, an error is shown and an empty list is set.
     */
    @PostConstruct
    public void init() {
        try {
            Integer customerId = getAuthenticatedCustomerId();
            loadCustomerOrders(customerId);
        } catch (AuthServiceException e) {
            showAuthError(e);
            orders = Collections.emptyList();
        }
    }

    public void cancelOrder(Order order) {
        try {
            requireSelectedOrder(order);
            this.selectedOrder = order;

            Integer customerId = getAuthenticatedCustomerId();
            orderService.cancelOwnOrderByCustomer(selectedOrder.getId(), customerId);

            GrowlUtil.success(GrowlTitles.SUCCESS_OPERATION_COMPLETED,
                    String.format(OrderMessages.ORDER_CANCELLED_SUCCESS, selectedOrder.getId()));

            loadCustomerOrders(customerId);
        } catch (ValidationException e) {
            GrowlUtil.warning(GrowlTitles.ERROR_OPERATION_FAILED, e.getMessage());
        } catch (AuthServiceException e) {
            showAuthError(e);
        } catch (OrderServiceException e) {
            handle(logger,
                    "order cancellation. Order ID: " + selectedOrder.getId(), e,
                    OrderMessages.CANCEL_ORDER_GENERIC_ERROR, Level.SEVERE);
        } catch (Exception e) {
            handle(logger, "unexpected error during order cancellation. Order ID: " + selectedOrder.getId(), e,
                    GrowlTitles.ERROR_OPERATION_FAILED, Level.SEVERE);
        }
    }

    public void clearOrderHistory() {
        try {
            Integer customerId = getAuthenticatedCustomerId();
            List<Order> processedOrders = orderService.getProcessedOrdersByCustomerId(customerId);

            if (processedOrders.isEmpty()) {
                GrowlUtil.warning(GrowlTitles.INFO_MESSAGE, OrderMessages.NO_PROCESSED_ORDERS_TO_CLEAR);
                return;
            }

            for (Order order : processedOrders) {
                archiveAndDelete(order);
            }

            GrowlUtil.success(GrowlTitles.SUCCESS_OPERATION_COMPLETED, OrderMessages.ORDER_HISTORY_CLEARED_SUCCESS);
            loadCustomerOrders(customerId);
        } catch (AuthServiceException e) {
            showAuthError(e);
        } catch (Exception e) {
            handle(logger, "clearing order history", e, GrowlTitles.ERROR_OPERATION_FAILED, Level.SEVERE);
        }
    }

    /**
     * Loads the full order, including its associated order details,
     * only if it belongs to the currently authenticated customer.
     * Stores the result for display purposes.
     *
     * @param orderId the ID of the order to retrieve
     */
    public void loadOrderDetailsById(Integer orderId) {
        try {
            requireOrderId(orderId);
            Integer customerId = getAuthenticatedCustomerId();
            selectedOrder = orderService.getByCustomerIdAndOrderId(customerId, orderId).orElse(null);
            requireSelectedOrder(selectedOrder);
            selectedOrderDetails = selectedOrder.getOrderDetails();
        } catch (ValidationException e) {
            GrowlUtil.warning(GrowlTitles.ERROR_OPERATION_FAILED, e.getMessage());
            selectedOrderDetails = Collections.emptyList();
        } catch (AuthServiceException e) {
            showAuthError(e);
            selectedOrderDetails = Collections.emptyList();
        } catch (Exception e) {
            handle(logger, "order detail loading", e, GrowlTitles.ERROR_OPERATION_FAILED, Level.SEVERE);
            selectedOrderDetails = Collections.emptyList();
        }
    }

    /**
     * Prepares an order for cancellation by loading its complete data,
     * including details, from the database, only if it belongs to the
     * currently authenticated customer. This method is intended to be
     * used prior to executing the actual cancel operation, for example
     * from a confirmation dialog.
     *
     * @param order the order to prepare
     */
    public void prepareCancelOrder(Order order) {
        try {
            requireSelectedOrder(order);
            Integer customerId = getAuthenticatedCustomerId();
            selectedOrder = orderService.getByCustomerIdAndOrderId(customerId, order.getId()).orElse(null);
        } catch (ValidationException e) {
            GrowlUtil.warning(GrowlTitles.ERROR_OPERATION_FAILED, e.getMessage());
        } catch (AuthServiceException e) {
            showAuthError(e);
        }
    }

    public boolean isOrdersEmpty() {
        return orders == null || orders.isEmpty();
    }

    public String getStatusSeverity(String status) {
        return OrderStatusUtil.getStatusSeverity(status);
    }

    private void loadCustomerOrders(Integer customerId) {
        try {
            orders = orderService.getOrdersByCustomer(customerId);
        } catch (Exception e) {
            handle(logger, "customer order loading", e,
                    OrderMessages.LOAD_ORDERS_GENERIC_ERROR, Level.SEVERE);
            orders = Collections.emptyList();
        }
    }

    /**
     * Ensures that a valid order is selected before attempting cancellation.
     * Displays a warning message if the order is null.
     *
     * @param order the order to validate
     * @return true if a valid order was selected, false otherwise
     */
    private boolean setSelectedOrderSafely(Order order) {
        this.selectedOrder = order;

        if (selectedOrder == null) {
            GrowlUtil.warning(GrowlTitles.ERROR_OPERATION_FAILED, OrderMessages.ORDER_NOT_FOUND_ERROR);
            return false;
        }
        return true;
    }

    /**
     * Retrieves the ID of the currently authenticated customer.
     * Throws an exception if the user is not authenticated or
     * not recognized as a customer.
     *
     * @return the authenticated customer's ID
     * @throws AuthServiceException if no customer is authenticated
     */
    private Integer getAuthenticatedCustomerId() throws AuthServiceException {
        return customerContext.requireCustomer().getId();
    }

    private void archiveAndDelete(Order order) {
        historicalOrderService.archiveOrder(order, order.getOrderDetails(), OrderArchivedReason.LIMPIEZA_HISTORIAL);
        orderService.deleteOrder(order.getId());
    }

    /**
     * Displays a standard error message when the user is not authenticated
     * or the session context is invalid for the current operation.
     *
     * @param e the thrown AuthServiceException
     */
    private void showAuthError(AuthServiceException e) {
        GrowlUtil.fromException(GrowlTitles.ERROR_OPERATION_FAILED, e);
    }

}
