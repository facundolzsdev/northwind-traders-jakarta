package com.northwind.model.dto;

import java.time.LocalDate;

import com.northwind.model.entity.Employee;
import lombok.*;

import static com.northwind.util.general.InputSanitizer.sanitizeText;

/**
 * Data Transfer Object representing editable employee profile information.
 * Contains all customer personal data that can be modified through the profile editor.
 * Maps 1:1 with {@link Employee} entity fields for personal information.
 */
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProfileDTO {

    private String fullName;
    private String dni;
    private LocalDate birthDate;
    private String phoneNumber;

    /**
     * Sets the full name after sanitizing the input to remove potentially dangerous characters.
     *
     * @param fullName Raw input that will be sanitized before storage
     */
    public void setFullName(String fullName) {
        this.fullName = sanitizeText(fullName);
    }

}
