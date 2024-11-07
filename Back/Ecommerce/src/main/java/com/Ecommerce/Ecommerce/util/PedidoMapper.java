package com.Ecommerce.Ecommerce.util;

import com.Ecommerce.Ecommerce.dto.UsuarioDTO;

import java.util.List;

import com.Ecommerce.Ecommerce.dto.ItemPedidoDTO;
import com.Ecommerce.Ecommerce.dto.PedidoDTO;
import com.Ecommerce.Ecommerce.entity.StatusPedido;
import com.Ecommerce.Ecommerce.entity.Usuario;
import com.Ecommerce.Ecommerce.entity.Pedido;

public class PedidoMapper {
    public static PedidoDTO toDTO(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        List<ItemPedidoDTO> itemPedidoDTO = ItemPedidoMapper.toListDTO(pedido.getItemPedido());
        UsuarioDTO usuarioDTO = UsuarioMapper.toDTO(pedido.getUsuario());

        return new PedidoDTO(
            pedido.getId(),
            pedido.getTotal(),
            pedido.getStatusPedido().name(),
            itemPedidoDTO,
            usuarioDTO
        );
    }

    public static Pedido toEntity(PedidoDTO dto) {
        if (dto == null) {
            return null;
        }

        Pedido pedido = new Pedido();
        pedido.setId(dto.getId());
        pedido.setTotal(dto.getTotal());
       if (dto.getStatusPedido() != null) {
            pedido.setStatusPedido(StatusPedido.valueOf(dto.getStatusPedido()));
        }

        if (dto.getUsuarioDTO() != null && dto.getUsuarioDTO().getId() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(dto.getUsuarioDTO().getId());
            pedido.setUsuario(usuario);
        }

        return pedido;
    }
}
