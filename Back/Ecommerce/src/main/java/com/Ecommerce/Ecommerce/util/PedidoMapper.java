package com.Ecommerce.Ecommerce.util;

import com.Ecommerce.Ecommerce.dto.StatusDTO;
import com.Ecommerce.Ecommerce.dto.UsuarioDTO;

import java.util.List;

import com.Ecommerce.Ecommerce.dto.ItemPedidoDTO;
import com.Ecommerce.Ecommerce.dto.PedidoDTO;
import com.Ecommerce.Ecommerce.entity.Status;
import com.Ecommerce.Ecommerce.entity.Usuario;
import com.Ecommerce.Ecommerce.entity.Pedido;

public class PedidoMapper {
    public static PedidoDTO toDTO(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        PedidoDTO.setStatusPedido(pedido.getStatusPedido().name());
        List<ItemPedidoDTO> itemPedidoDTO = ItemPedidoMapper.toListDTO(pedido.getItemPedido());
        UsuarioDTO usuarioDTO = UsuarioMapper.toDTO(pedido.getUsuario());

        return new PedidoDTO(
            pedido.getId(),
            pedido.getTotal(),
            statusPedido,
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

        if (dto.getStatusDTO() != null && dto.getStatusDTO().getId() != null) {
            Status status = new Status();
            status.setId(dto.getStatusDTO().getId());
            pedido.setStatusPedido(status);
        }

        if (dto.getUsuarioDTO() != null && dto.getUsuarioDTO().getId() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(dto.getUsuarioDTO().getId());
            pedido.setUsuario(usuario);
        }

        return pedido;
    }
}
