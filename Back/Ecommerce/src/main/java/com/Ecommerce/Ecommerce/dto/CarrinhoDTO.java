package com.Ecommerce.Ecommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class CarrinhoDTO {

    private Long id;
    private List<ItemPedidoDTO> itens;
    private Double total;
}
