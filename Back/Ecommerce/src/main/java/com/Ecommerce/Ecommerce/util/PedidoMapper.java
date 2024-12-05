package com.Ecommerce.Ecommerce.util;

import com.Ecommerce.Ecommerce.dto.*;
import com.Ecommerce.Ecommerce.entity.*;

import java.util.List;
import java.util.stream.Collectors;

// Importando os mappers necessários
import com.Ecommerce.Ecommerce.util.UsuarioMapper;
import com.Ecommerce.Ecommerce.util.CupomMapper;
import com.Ecommerce.Ecommerce.util.ItemPedidoMapper;

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
        dto.setStatusPedido(pedido.getStatusPedido().name());

        // Mapear o usuário
        if (pedido.getUsuario() != null) {
            UsuarioDTO usuarioDTO = UsuarioMapper.toDTO(pedido.getUsuario());
            dto.setUsuarioDTO(usuarioDTO);
        }

        // Mapear os itens do pedido
        if (pedido.getItemPedido() != null && !pedido.getItemPedido().isEmpty()) {
            List<ItemPedidoDTO> itensDTO = ItemPedidoMapper.toListDTO(pedido.getItemPedido());
            dto.setItemPedidoDTO(itensDTO);
        } else {
            dto.setItemPedidoDTO(List.of()); // Lista vazia
        }

        // Mapear o cupom aplicado
        if (pedido.getCupomAplicado() != null) {
            CupomDTO cupomDTO = CupomMapper.toDTO(pedido.getCupomAplicado());
            dto.setCupomAplicado(cupomDTO);
        }
        if (pedido.getEnderecoEntrega() != null) {
            EnderecoDTO enderecoDTO = EnderecoMapper.toDTO(pedido.getEnderecoEntrega());
            dto.setEnderecoEntrega(enderecoDTO);
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
        if (dto.getItemPedidoDTO() != null && !dto.getItemPedidoDTO().isEmpty()) {
            List<ItemPedido> itens = ItemPedidoMapper.toListEntity(dto.getItemPedidoDTO());
            pedido.setItemPedido(itens);
        }

        // Mapear o cupom aplicado
        if (dto.getCupomAplicado() != null) {
            Cupom cupom = CupomMapper.toEntity(dto.getCupomAplicado());
            pedido.setCupomAplicado(cupom);
        }

        if (dto.getEnderecoEntrega() != null) {
            Endereco endereco = EnderecoMapper.toEntity(dto.getEnderecoEntrega());
            pedido.setEnderecoEntrega(endereco);
        }
        return pedido;
    }

    public static void updateStatusFromDTO(Pedido pedido, UpdateStatusDTO updateStatusDTO) {
        if (updateStatusDTO.getStatusPedido() != null) {
            pedido.setStatusPedido(updateStatusDTO.getStatusPedido());
        }
    }
}
