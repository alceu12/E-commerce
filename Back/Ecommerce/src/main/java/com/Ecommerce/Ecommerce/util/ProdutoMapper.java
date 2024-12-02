package com.Ecommerce.Ecommerce.util;

import com.Ecommerce.Ecommerce.dto.CategoriaDTO;
import com.Ecommerce.Ecommerce.dto.ImagemDTO;
import com.Ecommerce.Ecommerce.dto.ProdutoDTO;
import com.Ecommerce.Ecommerce.entity.Categoria;
import com.Ecommerce.Ecommerce.entity.Imagem;
import com.Ecommerce.Ecommerce.entity.Produto;

import java.util.List;
import java.util.stream.Collectors;

public class ProdutoMapper {

    public static ProdutoDTO toDTO(Produto produto) {
        if (produto == null) {
            return null;
        }

        CategoriaDTO categoriaDTO = CategoriaMapper.toDTO(produto.getCategoria());

        List<ImagemDTO> imagensDTO = null;
        if (produto.getImagens() != null) {
            imagensDTO = produto.getImagens().stream()
                    .map(ImagemMapper::toDTO)
                    .collect(Collectors.toList());
        }


        return new ProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getValor(),
                produto.getEstoque(),
                categoriaDTO,
                imagensDTO
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

        if (dto.getCategoriaDTO() != null && dto.getCategoriaDTO().getId() != null) {
            Categoria categoria = new Categoria();
            categoria.setId(dto.getCategoriaDTO().getId());
            produto.setCategoria(categoria);
        }

        if (dto.getImagens() != null) {
            List<Imagem> imagens = dto.getImagens().stream()
                    .map(imagemDTO -> ImagemMapper.toEntity(imagemDTO, produto)) // Passando o produto
                    .collect(Collectors.toList());
            produto.setImagens(imagens);
        }

        return produto;
    }
}
