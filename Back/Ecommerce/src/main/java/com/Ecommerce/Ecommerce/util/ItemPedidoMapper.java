package com.Ecommerce.Ecommerce.util;

import com.Ecommerce.Ecommerce.dto.ProdutoDTO;
import com.Ecommerce.Ecommerce.dto.ItemPedidoDTO;
import com.Ecommerce.Ecommerce.entity.Produto;
import com.Ecommerce.Ecommerce.entity.ItemPedido;

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

public class ItemPedidoMapper {

    public static ItemPedidoDTO toDTO(ItemPedido itemPedido) {
        if (itemPedido == null) {
            return null;
        }

        ProdutoDTO produtoDTO = ProdutoMapper.toDTO(itemPedido.getProduto());

        return new ItemPedidoDTO(
                itemPedido.getId(),
                itemPedido.getQuantidade(),
                itemPedido.getPrecoUnitario(),
                produtoDTO
        );
    }

    public static List<ItemPedidoDTO> toListDTO(List<ItemPedido> itemPedidos) {
        if (itemPedidos == null || itemPedidos.isEmpty()) {
            return Collections.emptyList();
        }

        return itemPedidos.stream()
                .map(ItemPedidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static ItemPedido toEntity(ItemPedidoDTO dto) {
        if (dto == null) {
            return null;
        }

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(dto.getId());
        itemPedido.setQuantidade(dto.getQuantidade());
        itemPedido.setPrecoUnitario(dto.getPrecoUnitario());

        if (dto.getProdutoDTO() != null && dto.getProdutoDTO().getId() != null) {
            Produto produto = new Produto();
            produto.setId(dto.getProdutoDTO().getId());
            itemPedido.setProduto(produto);
        }

        return itemPedido;
    }

    public static List<ItemPedido> toListEntity(List<ItemPedidoDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return Collections.emptyList();
        }

        return dtoList.stream()
                .map(ItemPedidoMapper::toEntity)
                .collect(Collectors.toList());
    }
}
