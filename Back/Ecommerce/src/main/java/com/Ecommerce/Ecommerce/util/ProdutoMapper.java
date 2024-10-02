package com.Ecommerce.Ecommerce.util;

import com.Ecommerce.Ecommerce.dto.CategoriaDTO;
import com.Ecommerce.Ecommerce.dto.ProdutoDTO;
import com.Ecommerce.Ecommerce.entity.Categoria;
import com.Ecommerce.Ecommerce.entity.Produto;

public class ProdutoMapper {

    public static ProdutoDTO toDTO(Produto produto) {
        if (produto == null) {
            return null;
        }

        CategoriaDTO categoriaDTO = CategoriaMapper.toDTO(produto.getCategoria());

        return new ProdutoDTO(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getValor(),
            produto.getEstoque(),
            produto.getImagens(),
            categoriaDTO
        );
    }

    public static Produto toEntity(ProdutoDTO dto) {
        if (dto == null) {
            return null;
        }

        Produto produto = new Produto();
        produto.setId(dto.getId());
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setValor(dto.getValor());
        produto.setEstoque(dto.getEstoque());
        produto.setImagens(dto.getImagens());

        if (dto.getCategoriaDTO() != null && dto.getCategoriaDTO().getId() != null) {
            Categoria categoria = new Categoria();
            categoria.setId(dto.getCategoriaDTO().getId());
            produto.setCategoria(categoria);
        }

        return produto;
    }
}
