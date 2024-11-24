package com.Ecommerce.Ecommerce.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double total;
    private LocalDate dataPedido;
    private LocalDate dataEntrega;

    @Enumerated(EnumType.STRING)
    private StatusPedido statusPedido;

    @ManyToOne(cascade = CascadeType.PERSIST)-
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_pedido_id")
    private List<ItemPedido> itemPedido;

    @ManyToOne
    @JoinColumn(name = "cupom_id")
    private Cupom cupomAplicado;

}
