package com.northwind.model.enums;

public enum OrderStatus {
    PENDIENTE("Pendiente"),
    COMPLETADO("Completado"),
    RECHAZADO("Rechazado");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static OrderStatus fromLabel(String label) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.label.equalsIgnoreCase(label)) {
                return status;
            }
        }
        throw new IllegalArgumentException("OrderStatus not found for label: " + label);
    }

    @Override
    public String toString() {
        return this.label;
    }
}
