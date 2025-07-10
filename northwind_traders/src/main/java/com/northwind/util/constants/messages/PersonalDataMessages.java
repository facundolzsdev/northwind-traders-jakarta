package com.northwind.util.constants.messages;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PersonalDataMessages {

    public static final String INCOMPLETE_FIELDS =
            "Por favor, complete todos los campos del formulario para continuar.";

    public static final String INVALID_DNI =
            "El Documento nacional de identidad no tiene un formato válido o contiene caracteres no permitidos.";

    public static final String INVALID_POSTAL_CODE =
            "Código Postal inválido. Formato: letras, números, espacios y guiones. Longitud: entre 3 y 10 caracteres.";

    public static final String INVALID_PHONE_NUMBER =
            "El número de teléfono no tiene un formato válido.";

}
