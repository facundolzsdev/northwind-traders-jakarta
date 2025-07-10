package com.northwind.model.entity;

import com.northwind.model.support.Audit;
import com.northwind.model.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private Integer id;

    @Column(name = "UserName")
    private String username;

    @Column(name = "Email")
    private String email;

    @Column(name = "Password")
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RoleID", nullable = false)
    private Role role;

    @Embedded
    private Audit audit;

    public boolean hasRole(RoleType roleType) {
        return roleType != null && roleType.equals(this.role.getName());
    }

}
