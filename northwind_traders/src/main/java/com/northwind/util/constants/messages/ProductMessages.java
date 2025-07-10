package com.northwind.util.constants.messages;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductMessages {

    public static final String SUCCESS_CREATION =
            "El producto '%s' ha sido creado correctamente.";
    public static final String SUCCESS_UPDATE =
            "El producto '%s' ha sido editado correctamente.";

    public static final String ERROR_NOT_FOUND =
            "No se encontró el producto.";
    public static final String ERROR_CREATION =
            "No se pudo crear el nuevo producto.";
    public static final String ERROR_UPDATE =
            "No se pudo editar el producto con ID %s.";
    public static final String ERROR_ACTIVE_PRODUCT_NO_STOCK =
            "Un producto activo debe tener stock mayor a cero.";
    public static final String ERROR_INACTIVE_PRODUCT_HAS_STOCK =
            "Un producto inactivo no debe tener stock disponible.";

    public static final String NO_PRODUCT_DATA_CHANGES_DETECTED =
            "No hubo modificaciones en el producto %s.";

    public static final String WARN_INVALID_PRICE =
            "El precio debe ser un valor mayor que cero.";

}
