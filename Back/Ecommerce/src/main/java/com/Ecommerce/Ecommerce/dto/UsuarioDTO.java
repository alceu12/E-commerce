package com.Ecommerce.Ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String nome;
    private String email;
    @JsonIgnore
    private String password;
    private EnderecoDTO enderecoDTO;
    private FuncaoDTO funcaoDTO;
    private String statusUser;
}
