package com.Ecommerce.Ecommerce.entity;

public enum StatusPedido {

    PENDING_PAYMENT("Aguardando Pagamento"),
    PROCESSING("Processando"),
    SHIPPED("Enviado"),
    DELIVERED("Entregue"),
    CANCELED("Cancelado"),
    RETURN_REQUESTED("Devolução Solicitada"),
    RETURNED("Devolvido"),
    FAILED("Falhou");

    private final String description;
 
    StatusPedido(String description) {
        this.description = description;
    }
}
