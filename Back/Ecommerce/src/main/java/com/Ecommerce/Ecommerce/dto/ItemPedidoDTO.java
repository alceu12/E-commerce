package com.Ecommerce.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedidoDTO {

    private Long id;
    private int quantidade;
    private double precoUnitario;
    private ProdutoDTO produtoDTO;
}
