package com.northwind.model.enums;

public enum RoleType {
    ADMIN("Admin"),
    EMPLOYEE("Employee"),
    CUSTOMER("Customer");

    public final String label;

    RoleType(String label) {
        this.label = label;
    }

    public static RoleType fromLabel(String label) {
        for (RoleType role : RoleType.values()) {
            if (role.label.equalsIgnoreCase(label)) {
                return role;
            }
        }
        throw new IllegalArgumentException("RoleType not found for label: " + label);
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isEmployee() {
        return this == EMPLOYEE;
    }

    public boolean isCustomer() {
        return this == CUSTOMER;
    }

    @Override
    public String toString() {
        return this.label;
    }
}