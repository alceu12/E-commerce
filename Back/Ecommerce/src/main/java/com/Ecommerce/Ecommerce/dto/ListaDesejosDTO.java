package com.Ecommerce.Ecommerce.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListaDesejosDTO {
    private Long id;
    private UsuarioDTO usuarioDTO;
    private List<ProdutoDTO> produtos;
}
