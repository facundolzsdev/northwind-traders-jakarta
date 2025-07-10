package com.northwind.util.constants.messages;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DateMessages {

    public static final String WARN_INVALID_AGE =
            "Debe tener entre %d y %d años.";
    public static final String WARN_FUTURE_DATE =
            "No puede seleccionar una fecha futura.";

    public static final String ERROR_MUST_SELECT_BOTH_DATES =
            "Debe seleccionar ambas fechas.";
    public static final String ERROR_DATES_CANNOT_BE_EQUAL =
            "Las fechas seleccionadas no pueden ser iguales.";
    public static final String ERROR_END_DATE_BEFORE_START_DATE =
            "La fecha final no puede ser anterior a la inicial.";
}