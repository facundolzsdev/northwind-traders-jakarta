package com.northwind.util.constants.messages;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthMessages {

    public static final String ERROR_LOGIN =
            "Por favor, verifica tu información e inténtalo nuevamente.";
    public static final String GENERIC_ERROR_LOGIN =
            "Ha ocurrido un error durante el inicio de sesión. Por favor, inténtelo más tarde.";
    public static final String ERROR_SESSION_EXPIRED =
            "Tu sesión ha expirado. Por favor, inicia sesión nuevamente.";
    public static final String INACTIVE_EMPLOYEE =
            "Su cuenta de empleado se encuentra deshabilitada. Contacte con un Administrador.";

}
