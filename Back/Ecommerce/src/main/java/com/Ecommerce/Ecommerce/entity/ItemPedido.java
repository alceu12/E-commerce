package com.Ecommerce.Ecommerce.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ItemPedido {

    private Long id;
    private int quantidade;
    private double precoUnitario;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "produto_id", referencedColumnName = "id")
    private Produto produto;
}
