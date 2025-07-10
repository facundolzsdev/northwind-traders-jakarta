package com.northwind.util.constants.messages;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SignUpMessages {

    public static final String GENERIC_ERROR_SIGNUP =
            "Ocurrió un error durante el registro. Por favor, intente nuevamente más tarde.";

    public static final String SUCCESS_REGISTRATION =
            "Su registro ha sido exitoso. ¡Bienvenido, usuario '%s'!";

    public static final String REDIRECT_ERROR =
            "Registro exitoso! Sin embargo, no pudimos redirigirlo automáticamente. " +
                    "Recargue la página y regrese al login.";
}
