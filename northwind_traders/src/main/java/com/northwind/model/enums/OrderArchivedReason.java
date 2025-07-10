package com.northwind.model.enums;

public enum OrderArchivedReason {
    CLIENTE_ELIMINADO("Cliente Eliminado"),
    LIMPIEZA_HISTORIAL("Limpieza Historial"),
    LIMPIEZA_PROGRAMADA("Limpieza Programada");

    private final String label;

    OrderArchivedReason(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static OrderArchivedReason fromLabel(String label) {
        for (OrderArchivedReason reason : OrderArchivedReason.values()) {
            if (reason.label.equalsIgnoreCase(label)) {
                return reason;
            }
        }
        throw new IllegalArgumentException("OrderArchivedReason not found for label: " + label);
    }

    @Override
    public String toString() {
        return this.label;
    }
}
