package com.northwind.model.dto;

import com.northwind.model.entity.User;
import lombok.*;

/**
 * Data Transfer Object representing editable user account information.
 * Contains all user account data that can be modified through the profile editor.
 * Maps 1:1 with {@link User} entity fields for account information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountProfileDTO {
    private String email;
    private String originalEmail;
    private String username;
    private String originalUsername;
    private String password;
    private String confirmPassword;

    public UserAccountProfileDTO(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

}
