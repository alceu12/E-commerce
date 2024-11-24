package com.Ecommerce.Ecommerce.util;

import com.Ecommerce.Ecommerce.dto.CarrinhoDTO;
import com.Ecommerce.Ecommerce.entity.Carrinho;

import java.util.stream.Collectors;

public class CarrinhoMapper {

    public static CarrinhoDTO toDTO(Carrinho carrinho) {
        CarrinhoDTO dto = new CarrinhoDTO();
        dto.setId(carrinho.getId());
        dto.setItens(carrinho.getItens().stream().map(ItemPedidoMapper::toDTO).collect(Collectors.toList()));
        dto.setTotal(carrinho.getTotal());
        return dto;
    }
}
