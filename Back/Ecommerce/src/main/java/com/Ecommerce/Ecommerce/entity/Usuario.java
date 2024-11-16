package com.Ecommerce.Ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String password;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "funcao_id", referencedColumnName = "id")
    private Funcao funcao;

    @Enumerated(EnumType.STRING)
    private StatusUser statusUser;
    
    @ManyToMany
    @JoinTable(
            name = "usuario_cupons_usados",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "cupom_id")
    )
    private Set<Cupom> cuponsUsados = new HashSet<>();
}
