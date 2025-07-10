package com.northwind.service;

import com.northwind.exceptions.OrderServiceException;
import com.northwind.model.entity.Employee;
import com.northwind.model.entity.Order;
import com.northwind.model.entity.OrderDetail;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for handling order-related business logic.
 */
public interface iOrderService {

    List<Order> getAllOrders();

    Optional<Order> getOrderById(Integer id);

    void saveOrder(Order order, List<OrderDetail> details);

    void deleteOrder(Integer id);

    List<Order> getOrdersByCustomer(Integer customerId);

    Optional<Order> getByIdWithDetails(Integer id);

    Optional<Order> getByCustomerIdAndOrderId(Integer customerId, Integer orderId);

    List<Order> getProcessedOrdersByCustomerId(Integer customerId);

    List<Order> getAllPendingWithDetails();

    // ** SERVICE-LAYER SPECIFIC METHODS ** //

    /**
     * Persists a new order for the specified customer, including its details.
     *
     * @param customerId the ID of the customer placing the order
     * @param details    the list of order details for the new order
     * @return the persisted order
     * @throws OrderServiceException if the order cannot be created
     */
    Order persistNewOrderForCustomer(Integer customerId, List<OrderDetail> details);

    /**
     * Deletes an order and all its details directly, without ownership validation.
     * <p>
     * This method is for internal use in administrative or system operations,
     * such as deleting orders when deleting customer accounts or performing maintenance.
     *
     * <p> Does not perform authorization checks.</p>
     *
     * @param orderId ID of the order to delete.
     * @throws OrderServiceException if the order does not exist or deletion fails.
     */
    void deleteOrderAsSystem(Integer orderId);

    /**
     * Allows a customer to cancel one of their own orders, deleting it along with its details.
     * <p>
     * This method verifies that the order belongs to the customer requesting the cancellation
     * before proceeding to delete it from the database.
     *
     * <p>Intended use: Called from client interfaces where users have control over their active orders.</p>
     *
     * @param orderId    ID of the order to cancel.
     * @param customerId ID of the customer requesting the cancellation.
     * @throws OrderServiceException if the order does not exist or does not belong to the customer.
     */
    void cancelOwnOrderByCustomer(Integer orderId, Integer customerId);

    /**
     * Approves an order by marking its status as Completed.
     * <p>
     * This method is intended to be called by employees during the order review process.
     * It does not perform any ownership or authorization checks, assuming trusted context.
     *
     * <p>Intended use: Back-office or admin-side interfaces where employees confirm pending orders.</p>
     *
     * @param orderId  ID of the order to approve.
     * @param employee The employee performing the approval; will be recorded on the order.
     * @throws OrderServiceException if the order does not exist or the operation fails.
     */
    void approveOrder(Integer orderId, Employee employee);

    /**
     * Rejects a pending order by changing its status to Rejected.
     * <p>
     * This method is used by employees or the system to formally deny
     * a customer's order without deleting it, leaving a visible record.
     *
     * <p>Does not perform ownership or authorization validation.</p>
     *
     * @param orderId  ID of the order to reject.
     * @param employee The employee performing the rejection; will be recorded on the order.
     * @throws OrderServiceException if the order does not exist or the rejection fails.
     */
    void rejectOrder(Integer orderId, Employee employee);

}
