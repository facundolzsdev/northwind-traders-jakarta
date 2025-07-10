package com.northwind.model.support;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Embeddable
public class Audit {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static Audit createDefaultAudit() {
        Audit audit = new Audit();
        audit.prePersist();
        return audit;
    }

    private String formatCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        return (createdAt != null) ? createdAt.format(formatter) : "No disponible";
    }

    private String formatLastUpdated() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        return (updatedAt != null) ? updatedAt.format(formatter) : "No fue modificado.";
    }

    @Override
    public String toString() {
        return "Registro creado en: " + formatCreatedAt() + ". Modificado en: " + formatLastUpdated();
    }
}