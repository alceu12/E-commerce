// src/main/java/com/Ecommerce/Ecommerce/entity/StatusPedido.java
package com.Ecommerce.Ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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

    @JsonValue
    public String getDescription() {
        return description;
    }
}
