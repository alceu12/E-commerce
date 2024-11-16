package com.Ecommerce.Ecommerce.util;

import com.Ecommerce.Ecommerce.dto.CupomDTO;
import com.Ecommerce.Ecommerce.entity.Cupom;

public class CupomMapper {

    public static CupomDTO toDTO(Cupom cupom) {
        if (cupom == null) {
            return null;
        }

        CupomDTO dto = new CupomDTO();
        dto.setId(cupom.getId());
        dto.setCodigo(cupom.getCodigo());
        dto.setValorDesconto(cupom.getValorDesconto());
        dto.setValorMinimo(cupom.getValorMinimo());
        dto.setDataInicio(cupom.getDataInicio());
        dto.setDataFim(cupom.getDataFim());
        dto.setAtivo(cupom.isAtivo());


        return dto;
    }

    public static Cupom toEntity(CupomDTO dto) {
        if (dto == null) {
            return null;
        }

        Cupom cupom = new Cupom();
        cupom.setId(dto.getId());
        cupom.setCodigo(dto.getCodigo());
        cupom.setValorDesconto(dto.getValorDesconto());
        cupom.setValorMinimo(dto.getValorMinimo());
        cupom.setDataInicio(dto.getDataInicio());
        cupom.setDataFim(dto.getDataFim());
        cupom.setAtivo(dto.isAtivo());

        return cupom;
    }
}
