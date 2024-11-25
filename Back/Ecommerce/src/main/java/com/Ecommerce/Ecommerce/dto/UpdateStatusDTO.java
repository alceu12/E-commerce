package com.Ecommerce.Ecommerce.dto;

import com.Ecommerce.Ecommerce.entity.StatusPedido;

public class UpdateStatusDTO {
    private StatusPedido statusPedido;

    // Getters e Setters
    public StatusPedido getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(StatusPedido statusPedido) {
        this.statusPedido = statusPedido;
    }
}