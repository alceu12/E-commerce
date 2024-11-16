package com.Ecommerce.Ecommerce.util;

import com.Ecommerce.Ecommerce.dto.PedidoDTO;
import com.Ecommerce.Ecommerce.dto.CupomDTO;
import com.Ecommerce.Ecommerce.dto.UsuarioDTO;
import com.Ecommerce.Ecommerce.dto.ItemPedidoDTO;
import com.Ecommerce.Ecommerce.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class PedidoMapper {

    public static PedidoDTO toDTO(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setTotal(pedido.getTotal());
        dto.setDataPedido(pedido.getDataPedido());
        dto.setDataEntrega(pedido.getDataEntrega());
        dto.setStatusPedido(String.valueOf(pedido.getStatusPedido()));

        // Mapear o usuário
        if (pedido.getUsuario() != null) {
            UsuarioDTO usuarioDTO = UsuarioMapper.toDTO(pedido.getUsuario());
            dto.setUsuarioDTO(usuarioDTO);
        }

        // Mapear os itens do pedido
        if (pedido.getItemPedido() != null) {
            List<ItemPedidoDTO> itensDTO = pedido.getItemPedido().stream()
                    .map(ItemPedidoMapper::toDTO)
                    .collect(Collectors.toList());
            dto.setItemPedidoDTO(itensDTO);
        }

        // Mapear o cupom aplicado
        if (pedido.getCupomAplicado() != null) {
            CupomDTO cupomDTO = CupomMapper.toDTO(pedido.getCupomAplicado());
            dto.setCupomAplicado(cupomDTO);
        }

        return dto;
    }

    public static Pedido toEntity(PedidoDTO dto) {
        if (dto == null) {
            return null;
        }

        Pedido pedido = new Pedido();
        pedido.setId(dto.getId());
        pedido.setTotal(dto.getTotal());
        pedido.setStatusPedido(StatusPedido.valueOf(dto.getStatusPedido()));
        pedido.setDataPedido(dto.getDataPedido());
        pedido.setDataEntrega(dto.getDataEntrega());

        // Mapear o usuário
        if (dto.getUsuarioDTO() != null) {
            Usuario usuario = UsuarioMapper.toEntity(dto.getUsuarioDTO());
            pedido.setUsuario(usuario);
        }

        // Mapear os itens do pedido
        if (dto.getItemPedidoDTO() != null) {
            List<ItemPedido> itens = dto.getItemPedidoDTO().stream()
                    .map(ItemPedidoMapper::toEntity)
                    .collect(Collectors.toList());
            pedido.setItemPedido(itens);
        }

        // Mapear o cupom aplicado
        if (dto.getCupomAplicado() != null) {
            Cupom cupom = CupomMapper.toEntity(dto.getCupomAplicado());
            pedido.setCupomAplicado(cupom);
        }

        return pedido;
    }
}
