package com.Ecommerce.Ecommerce.util;

import com.Ecommerce.Ecommerce.dto.CategoriaDTO;
import com.Ecommerce.Ecommerce.entity.Categoria;

public class CategoriaMapper {

    public static CategoriaDTO toDTO(Categoria categoria) {
        if (categoria == null) {
            return null;
        }

        return new CategoriaDTO(
                categoria.getId(),
                categoria.getNome());
    }

    public static Categoria toEntity(CategoriaDTO dto) {
        if (dto == null) {
            return null;
        }

        Categoria categoria = new Categoria();
        categoria.setId(dto.getId());
        categoria.setNome(dto.getNome());

        return categoria;
    }
}
