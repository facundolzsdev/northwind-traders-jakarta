package com.northwind.util.constants.messages;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderMessages {

    public static final String ORDER_CANCELLED_SUCCESS =
            "El pedido n° %s ha sido cancelado.";

    public static final String CANCEL_ORDER_GENERIC_ERROR =
            "Ha ocurrido un error al cancelar el pedido. Por favor, inténtelo de nuevo.";

    public static final String LOAD_ORDERS_GENERIC_ERROR =
            "Tuvimos problemas para cargar sus pedidos. Por favor, inténtelo de nuevo.";

    public static final String ORDER_PROCESSING_ERROR =
            "Tuvimos problemas al procesar tu pedido. Por favor, inténtelo de nuevo.";

    public static final String ORDER_NOT_FOUND_ERROR =
            "No se pudo encontrar el número de orden.";

    public static final String ORDER_HISTORY_CLEARED_SUCCESS =
            "Su historial de órdenes procesadas ha sido limpiado.";

    public static final String NO_PROCESSED_ORDERS_TO_CLEAR =
            "No se encontraron órdenes procesadas para limpiar.";

    public static final String ORDER_ACCEPTED_SUCCESS =
            "El pedido n° %s ha sido aprobado.";

    public static final String ACCEPT_ORDER_GENERIC_ERROR =
            "Ha ocurrido un error al intentar aprobar este pedido.";

    public static final String ORDER_REJECTED_SUCCESS =
            "El pedido n° %s ha sido rechazado.";

    public static final String REJECT_ORDER_GENERIC_ERROR =
            "Ha ocurrido un error al intentar rechazar este pedido.";
}