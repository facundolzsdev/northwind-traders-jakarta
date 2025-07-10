package com.northwind.util.validation;

import com.northwind.model.entity.Customer;
import com.northwind.model.entity.Employee;
import com.northwind.model.dto.CustomerProfileDTO;
import com.northwind.model.dto.EmployeeProfileDTO;
import com.northwind.util.constants.messages.GrowlTitles;
import com.northwind.util.constants.messages.PersonalDataMessages;
import com.northwind.util.jsf.GrowlUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Utility class that validates personal information for both customers and employees
 * during registration and update processes.
 *
 * <p>Key validations include:
 * <ul>
 *   <li>Required field checks</li>
 *   <li>Phone number format validation</li>
 *   <li>Postal code validation</li>
 *   <li>DNI (National ID) validation</li>
 *   <li>Birth date validation</li>
 * </ul>
 */
@ApplicationScoped
public class PersonalInfoValidator {

    @Inject
    private DateValidator dateValidator;

    /**
     * Validates a Customer entity's personal information fields.
     *
     * @param customer The Customer entity to validate
     * @return true if all fields are valid, false otherwise
     */
    public boolean validateCustomerFields(Customer customer) {
        return validateCustomerData(customer.getFullName(),
                customer.getCountry(), customer.getCity(),
                customer.getPostalCode(), customer.getAddress(),
                customer.getPhoneNumber());
    }

    /**
     * Validates a CustomerProfileDTO personal information fields.
     *
     * @param customerDTO The Customer DTO to validate
     * @return true if all fields are valid, false otherwise
     */
    public boolean validateCustomerProfile(CustomerProfileDTO customerDTO) {
        return validateCustomerData(customerDTO.getFullName(),
                customerDTO.getCountry(), customerDTO.getCity(),
                customerDTO.getPostalCode(), customerDTO.getAddress(),
                customerDTO.getPhoneNumber());
    }

    /**
     * Validates an EmployeeProfileDTO personal information fields including DNI and birth date.
     *
     * @param employeeDTO The Employee DTO to validate
     * @return true if all fields are valid, false otherwise
     */
    public boolean validateEmployeeProfile(EmployeeProfileDTO employeeDTO) {
        return validateEmployeeData(employeeDTO.getFullName(), employeeDTO.getDni(),
                employeeDTO.getBirthDate(), employeeDTO.getPhoneNumber());
    }

    /**
     * Validates an Employee entity's personal information fields.
     *
     * @param employee The Employee entity to validate
     * @return true if all fields are valid, false otherwise
     */
    public boolean validateEmployeeFields(Employee employee) {
        return validateEmployeeData(employee.getFullName(), employee.getDni(),
                employee.getBirthDate(), employee.getPhoneNumber()
        );
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        return validateFieldAgainstPattern(phoneNumber, ValidationPatterns.PHONE, PersonalDataMessages.INVALID_PHONE_NUMBER);
    }

    public boolean validatePostalCode(String postalCode) {
        return validateFieldAgainstPattern(postalCode, ValidationPatterns.POSTAL_CODE, PersonalDataMessages.INVALID_POSTAL_CODE);
    }

    public boolean validateDNI(String dni) {
        return validateFieldAgainstPattern(dni, ValidationPatterns.DNI, PersonalDataMessages.INVALID_DNI);
    }

    /**
     * Checks if all provided fields are non-null and non-empty.
     *
     * @param fields Variable number of strings to check
     * @return false if any field is null or empty (displays warning message)
     */
    public boolean validateAllFieldsAreFilled(String... fields) {
        if (Stream.of(fields).anyMatch(field -> field == null || field.trim().isEmpty())) {
            GrowlUtil.warning(GrowlTitles.WARN_TITLE, PersonalDataMessages.INCOMPLETE_FIELDS);
            return false;
        }
        return true;
    }

    /**
     * Internal validation for customer data fields
     */
    private boolean validateCustomerData(String fullName, String country, String city,
                                         String postalCode, String address, String phoneNumber) {
        return validateAllFieldsAreFilled(fullName, country, city, postalCode, address, phoneNumber)
                && validatePhoneNumber(phoneNumber)
                && validatePostalCode(postalCode);
    }

    /**
     * Internal validation for employee data fields
     */
    private boolean validateEmployeeData(String fullName, String dni, LocalDate birthDate, String phoneNumber) {
        return validateAllFieldsAreFilled(
                fullName, dni, birthDate != null ? birthDate.toString() : "", phoneNumber
        ) &&
                validateDNI(dni) &&
                validatePhoneNumber(phoneNumber) &&
                dateValidator.isValidBirthDate(birthDate);
    }

    /**
     * Generic field validation against a regex pattern
     *
     * @param value        The value to validate
     * @param pattern      The regex pattern to match
     * @param errorMessage Message to display if validation fails
     * @return true if value matches the pattern
     */
    private boolean validateFieldAgainstPattern(String value, Pattern pattern, String errorMessage) {
        if (value == null || !pattern.matcher(value).matches()) {
            GrowlUtil.warning(GrowlTitles.WARN_TITLE, errorMessage);
            return false;
        }
        return true;
    }
}
