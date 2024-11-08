package com.Ecommerce.Ecommerce.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoDTO {

    private Long id;
    private String nome;
    private String descricao;
    private double valor;
    private int estoque;
    private CategoriaDTO categoriaDTO;
    private List<ImagemDTO> imagens;
}
