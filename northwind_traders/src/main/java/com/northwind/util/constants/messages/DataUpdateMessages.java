package com.northwind.util.constants.messages;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DataUpdateMessages {

    public static final String NO_DATA_CHANGES_DETECTED =
            "No se detectaron modificaciones en los datos.";

    public static final String DATA_UPDATE_SUCCESS =
            "Los datos fueron actualizados correctamente.";

    public static final String ACCOUNT_DELETION_SUCCESS =
            "La cuenta de usuario fue eliminada correctamente.";

    public static final String DATA_UPDATE_FAILURE =
            "No se pudieron actualizar los datos. Intente nuevamente.";

    public static final String ACCOUNT_DELETION_FAILURE =
            "Ocurrió un problema al intentar eliminar la cuenta. Por favor, inténtelo nuevamente más tarde.";

}
