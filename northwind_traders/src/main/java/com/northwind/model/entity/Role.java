package com.northwind.model.entity;

import com.northwind.model.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleID")
    private Byte id;

    @Column(name = "RoleName")
    @Enumerated(EnumType.STRING)
    private RoleType name;

}
