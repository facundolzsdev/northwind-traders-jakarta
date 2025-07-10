package com.northwind.util.converter;

import jakarta.faces.convert.FacesConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;

/**
 * JSF converter for {@link LocalDate}.
 * Formats dates as "dd/MM/yyyy" and parses them back.
 */
@FacesConverter(value = "dateConverter")
public class DateConverter implements Converter<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Converts a {@link LocalDate} to a formatted String.
     *
     * @param value the LocalDate to format
     * @return formatted date string or empty if null
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, LocalDate value) {
        if (value == null) {
            return "";
        }
        return value.format(FORMATTER);
    }

    /**
     * Parses a formatted date string into a {@link LocalDate}.
     *
     * @param value the date string to parse
     * @return parsed LocalDate or null if empty
     */
    @Override
    public LocalDate getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(value, FORMATTER);
    }

}

