package com.northwind.controller.employee;

import com.northwind.context.EmployeeContext;
import com.northwind.exceptions.OrderServiceException;
import com.northwind.exceptions.ValidationException;
import com.northwind.model.entity.Employee;
import com.northwind.model.entity.Order;
import com.northwind.model.entity.OrderDetail;
import com.northwind.service.iOrderService;
import com.northwind.util.jsf.GrowlUtil;

import static com.northwind.util.exception.BeanExceptionUtil.handle;
import static com.northwind.util.validation.OrderReviewValidator.*;

import com.northwind.util.general.OrderStatusUtil;
import com.northwind.util.constants.messages.GrowlTitles;
import com.northwind.util.constants.messages.OrderMessages;

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
 * Managed bean for order review operations performed by employees.
 * <p>
 * Handles the review, acceptance, and rejection of customer orders.
 * It loads all pending orders with details and allows employees to inspect and manage them.
 * </p>
 */
@Getter
@Setter
@Named
@ViewScoped
public class OrderReviewBean implements Serializable {

    private List<Order> pendingOrders;
    private Order selectedOrder;
    private List<OrderDetail> selectedOrderDetails;

    @Inject
    private iOrderService orderService;
    @Inject
    private EmployeeContext employeeContext;
    @Inject
    private Logger logger;

    @PostConstruct
    public void init() {
        loadPendingOrders();
    }

    public void acceptOrder() {
        try {
            requireSelectedOrder(selectedOrder);
            Employee employee = getAuthenticatedEmployee();
            orderService.approveOrder(selectedOrder.getId(), employee);
            GrowlUtil.success(GrowlTitles.SUCCESS_OPERATION_COMPLETED,
                    String.format(OrderMessages.ORDER_ACCEPTED_SUCCESS, selectedOrder.getId()));
            reloadOrdersAndReset();
        } catch (ValidationException ve) {
            showNoOrderSelected();
        } catch (OrderServiceException e) {
            handle(logger, "accepting order", e, OrderMessages.ACCEPT_ORDER_GENERIC_ERROR, Level.SEVERE);
        }
    }

    public void rejectOrder() {
        try {
            requireSelectedOrder(selectedOrder);
            Employee employee = getAuthenticatedEmployee();
            orderService.rejectOrder(selectedOrder.getId(), employee);
            GrowlUtil.success(GrowlTitles.SUCCESS_OPERATION_COMPLETED,
                    String.format(OrderMessages.ORDER_REJECTED_SUCCESS, selectedOrder.getId()));
            reloadOrdersAndReset();
        } catch (ValidationException ve) {
            showNoOrderSelected();
        } catch (OrderServiceException e) {
            handle(logger, "rejecting order", e, OrderMessages.REJECT_ORDER_GENERIC_ERROR, Level.SEVERE);
        }
    }

    public void loadOrderDetails(Integer orderId) {
        try {
            requireOrderId(orderId);
            selectedOrder = orderService.getByIdWithDetails(orderId).orElse(null);
            selectedOrderDetails = selectedOrder != null
                    ? selectedOrder.getOrderDetails()
                    : Collections.emptyList();
        } catch (ValidationException ve) {
            showNoOrderSelected();
        } catch (Exception e) {
            handle(logger, "loading order details", e, OrderMessages.LOAD_ORDERS_GENERIC_ERROR, Level.SEVERE);
        }
    }

    public boolean isPendingOrdersEmpty() {
        return pendingOrders == null || pendingOrders.isEmpty();
    }

    public String getStatusSeverity(String status) {
        return OrderStatusUtil.getStatusSeverity(status);
    }

    private void loadPendingOrders() {
        try {
            pendingOrders = orderService.getAllPendingWithDetails();
        } catch (Exception e) {
            handle(logger, "loading pending orders", e, OrderMessages.LOAD_ORDERS_GENERIC_ERROR, Level.SEVERE);
            pendingOrders = Collections.emptyList();
        }
    }

    private void reloadOrdersAndReset() {
        loadPendingOrders();
        resetSelection();
    }

    private void resetSelection() {
        selectedOrder = null;
        selectedOrderDetails = null;
    }

    private Employee getAuthenticatedEmployee() {
        return employeeContext.requireEmployee();
    }

    private void showNoOrderSelected() {
        GrowlUtil.warning(GrowlTitles.ERROR_OPERATION_FAILED, OrderMessages.ORDER_NOT_FOUND_ERROR);
    }
}
