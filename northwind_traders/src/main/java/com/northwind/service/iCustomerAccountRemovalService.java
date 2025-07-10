package com.northwind.service;

import com.northwind.exceptions.AccountDeletionException;

/**
 * Service interface for handling customer account deletion logic.
 * <p>
 * This service manages the removal of customer-related data, ensuring that all
 * associated entities such as the user account, shopping cart, and cart items
 * are properly cleaned up from the database.
 */
public interface iCustomerAccountRemovalService {

    /**
     * Deletes a customer account along with all related data from the system.
     * <p>
     * This method ensures complete cleanup of a customer by:
     * <ul>
     *     <li>Retrieving and validating the existence of the customer using the provided ID.</li>
     *     <li>Deleting the customer's shopping cart, including any cart items it may contain.</li>
     *     <li>Deleting the customer entity itself along with its associated user account.</li>
     * </ul>
     * <p>
     * Note: Any existing orders linked to the customer are not removed from the database.
     * Instead, their foreign key reference to the customer is set to {@code NULL} to preserve the order history.
     *
     * @param customerId the ID of the customer whose account and all associated data will be deleted.
     * @throws AccountDeletionException if the customer does not exist or if any error occurs during the deletion process.
     */
    void deleteCustomerAccount(Integer customerId);

}
