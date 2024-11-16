package com.Ecommerce.Ecommerce.util;

import com.Ecommerce.Ecommerce.dto.ImagemDTO;
import com.Ecommerce.Ecommerce.entity.Imagem;
import com.Ecommerce.Ecommerce.entity.Produto;

public class ImagemMapper {

    public static ImagemDTO toDTO(Imagem imagem) {
        if (imagem == null) {
            return null;
        }

        ImagemDTO dto = new ImagemDTO();
        dto.setId(imagem.getId());
        dto.setUrl("/imagens/" + imagem.getId());
        dto.setDados(imagem.getDados());

        return dto;
    }

    public static Imagem toEntity(ImagemDTO dto, Produto produto) {
        if (dto == null) {
            return null;
        }

        Imagem imagem = new Imagem();
        imagem.setId(dto.getId());
        imagem.setDados(dto.getDados());
        imagem.setProduto(produto);

        return imagem;
    }
}
