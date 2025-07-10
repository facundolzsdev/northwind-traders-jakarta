package com.northwind.util.constants.messages;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserAccountMessages {

    public static final String INVALID_EMAIL_ADDRESS =
            "La dirección de correo electrónico no es válida o está mal escrita.";

    public static final String INVALID_PASSWORD =
            "La contraseña debe tener al menos 8 caracteres, incluir una letra mayúscula y al menos un número.";

    public static final String INVALID_USERNAME =
            "El nombre de usuario no tiene un formato válido o contiene caracteres no permitidos.";

    public static final String PASSWORDS_DO_NOT_MATCH =
            "Las contraseñas ingresadas no coinciden.";

    public static final String EMAIL_ALREADY_EXISTS =
            "El correo electrónico ya está registrado.";

    public static final String USERNAME_ALREADY_EXISTS =
            "El nombre de usuario ya está en uso. Por favor, ingrese otro.";
}
