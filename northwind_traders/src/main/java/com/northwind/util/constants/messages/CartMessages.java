package com.northwind.util.constants.messages;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CartMessages {

    public static final String SUCCESS_ADD_TO_CART =
            "El producto '%s' ha sido añadido al carrito. Cantidad: %d.";
    public static final String SUCCESS_CLEAR_CART =
            "El carrito ha sido vaciado.";
    public static final String SUCCESS_REMOVE_FROM_CART =
            "El producto '%s' ha sido eliminado del carrito.";
    public static final String SUCCESS_PURCHASE =
            "Tu compra ha sido realizada. Puedes ver la orden en \"Mis Pedidos\".";

    public static final String ERROR_INSUFFICIENT_STOCK =
            "No hay suficiente stock del producto '%s' para la cantidad seleccionada.";

    public static final String ERROR_ADD_TO_CART =
            "Se ha producido un error al agregar el producto al carrito.";
    public static final String ERROR_CLEAR_CART =
            "Se ha producido un error al vaciar el carrito.";
    public static final String ERROR_REMOVE_FROM_CART =
            "Se ha producido un error al eliminar el producto del carrito.";
    public static final String ERROR_UPDATE_CART =
            "No se pudo actualizar la cantidad del producto en el carrito. Por favor, inténtelo de nuevo.";

    public static final String CART_EMPTY_ADVICE =
            "El carrito de compras está vacío.";
    public static final String ERROR_INITIALIZING_CART =
            "Error al inicializar el carrito. Por favor, inténtelo de nuevo.";
    public static final String ERROR_NO_CUSTOMER_FOUND =
            "Hubo un problema al cargar su cuenta. Por favor, inténtelo nuevamente más tarde.";
    public static final String ERROR_LOADING_CART =
            "Ocurrió un problema al cargar su carrito. Por favor, inténtelo nuevamente más tarde.";

}
