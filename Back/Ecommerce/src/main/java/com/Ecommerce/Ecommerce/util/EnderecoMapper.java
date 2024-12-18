package com.Ecommerce.Ecommerce.util;

import com.Ecommerce.Ecommerce.dto.EnderecoDTO;
import com.Ecommerce.Ecommerce.entity.Endereco;

public class EnderecoMapper {
    public static EnderecoDTO toDTO(Endereco endereco) {
        if (endereco == null) {
            return null;
        }

        return new EnderecoDTO(
                endereco.getId(),
                endereco.getCep(),
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado()
        );
    }

    public static Endereco toEntity(EnderecoDTO dto) {
        if (dto == null) {
            return null;
        }

        Endereco endereco = new Endereco();
        endereco.setId(dto.getId());
        endereco.setCep(dto.getCep());
        endereco.setRua(dto.getRua());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());

        return endereco;
    }
}
