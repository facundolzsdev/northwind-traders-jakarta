package com.northwind.model.dto;

import com.northwind.model.entity.Customer;

import static com.northwind.util.general.InputSanitizer.*;

import lombok.*;

/**
 * Data Transfer Object representing editable customer profile information.
 * Contains all customer personal data that can be modified through the profile editor.
 * Maps 1:1 with {@link Customer} entity fields for personal information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileDTO {

    private String fullName;
    private String address;
    private String country;
    private String city;
    private String phoneNumber;
    private String postalCode;

    /**
     * Sets the full name after sanitizing the input to remove potentially dangerous characters.
     *
     * @param fullName Raw input that will be sanitized before storage
     */
    public void setFullName(String fullName) {
        this.fullName = sanitizeText(fullName);
    }

    /**
     * Sets the address after applying address-specific sanitization.
     *
     * @param address Raw input that will be sanitized while preserving valid address characters
     */
    public void setAddress(String address) {
        this.address = sanitizeAlphaNumericInput(address);
    }

    /**
     * Sets the country name after sanitizing the input.
     *
     * @param country Raw input that will be sanitized before storage
     */
    public void setCountry(String country) {
        this.country = sanitizeText(country);
    }

    /**
     * Sets the city name after sanitizing the input.
     *
     * @param city Raw input that will be sanitized before storage
     */
    public void setCity(String city) {
        this.city = sanitizeText(city);
    }
}