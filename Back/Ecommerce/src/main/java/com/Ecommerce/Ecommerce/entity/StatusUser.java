package com.Ecommerce.Ecommerce.entity;

public enum StatusUser {
    ACTIVE("Ativo"),
    INACTIVE("Inativo"),
    SUSPENDED("Suspenso"),
    PENDING_VERIFICATION("Aguardando Verificação"),
    DEACTIVATED("Desativado"),
    BANNED("Banido"),
    DELETED("Excluído");

    private final String description;

    StatusUser(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
