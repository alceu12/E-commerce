package com.Ecommerce.Ecommerce.util;

import java.util.List;
import java.util.stream.Collectors;
import com.Ecommerce.Ecommerce.dto.ListaDesejosDTO;
import com.Ecommerce.Ecommerce.dto.ProdutoDTO;
import com.Ecommerce.Ecommerce.dto.UsuarioDTO;
import com.Ecommerce.Ecommerce.entity.ListaDesejos;
import com.Ecommerce.Ecommerce.entity.Produto;

public class ListaDesejosMapper {
    public static ListaDesejosDTO toDTO(ListaDesejos listaDesejos) {
        if (listaDesejos == null) {
            return null;
        }

        List<ProdutoDTO> produtosDTO = listaDesejos.getProdutos()
                .stream()
                .map(ProdutoMapper::toDTO)
                .collect(Collectors.toList());

        UsuarioDTO usuarioDTO = UsuarioMapper.toDTO(listaDesejos.getUsuario());

        return new ListaDesejosDTO(
            listaDesejos.getId(),
            usuarioDTO,
            produtosDTO
        );
    }

    public static ListaDesejos toEntity(ListaDesejosDTO dto) {
        if (dto == null) {
            return null;
        }

        ListaDesejos listaDesejos = new ListaDesejos();
        listaDesejos.setId(dto.getId());

        if (dto.getUsuarioDTO() != null && dto.getUsuarioDTO().getId() != null) {
            listaDesejos.setUsuario(UsuarioMapper.toEntity(dto.getUsuarioDTO()));
        }

        List<Produto> produtos = dto.getProdutos()
                .stream()
                .map(ProdutoMapper::toEntity)
                .collect(Collectors.toList());

        listaDesejos.setProdutos(produtos);

        return listaDesejos;
    }
}
