package com.Ecommerce.Ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Cupom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codigo; // Código do cupom

    private double valorDesconto; // Valor do desconto (porcentagem ou valor fixo)

    private double valorMinimo;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    private boolean ativo;

    @ManyToMany(mappedBy = "cuponsUsados")
    private Set<Usuario> usuariosQueUsaram = new HashSet<>();
}
