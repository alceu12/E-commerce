package com.Ecommerce.Ecommerce.util;

import com.Ecommerce.Ecommerce.dto.FuncaoDTO;
import com.Ecommerce.Ecommerce.entity.Funcao;

public class FuncaoMapper {
    public static FuncaoDTO toDTO(Funcao funcao) {
        if (funcao == null) {
            return null;
        }

        return new FuncaoDTO(
                funcao.getId(),
                funcao.getNome());
    }

    public static Funcao toEntity(FuncaoDTO dto) {
        if (dto == null) {
            return null;
        }

        Funcao funcao = new Funcao();
        funcao.setId(dto.getId());
        funcao.setNome(dto.getNome());

        return funcao;
    }
}
