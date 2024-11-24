package com.Ecommerce.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CupomDTO {

    private Long id;
    private String codigo;
    private double valorDesconto;
    private double valorMinimo;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean ativo;

}