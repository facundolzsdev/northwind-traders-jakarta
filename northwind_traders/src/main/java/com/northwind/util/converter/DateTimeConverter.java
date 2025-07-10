package com.northwind.util.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * JSF converter for {@link LocalDateTime}.
 * Formats dates as "dd/MM/yyyy - HH:mm" and parses them back.
 */
@FacesConverter(value = "dateTimeConverter")
public class DateTimeConverter implements Converter<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");

    /**
     * Converts a {@link LocalDateTime} to a formatted String.
     *
     * @param value the LocalDateTime to format
     * @return formatted date string or empty if null
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, LocalDateTime value) {
        if (value == null) {
            return "";
        }
        return value.format(FORMATTER);
    }

    /**
     * Parses a formatted date string into a {@link LocalDateTime}.
     *
     * @param value the date string to parse
     * @return parsed LocalDateTime or null if empty
     */
    @Override
    public LocalDateTime getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(value, FORMATTER);
    }

}
