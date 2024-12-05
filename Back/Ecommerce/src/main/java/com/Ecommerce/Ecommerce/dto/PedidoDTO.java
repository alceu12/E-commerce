package com.Ecommerce.Ecommerce.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {

    private Long id;
    private double total;
    private String statusPedido;
    private LocalDate dataPedido;
    private LocalDate dataEntrega;
    private List<ItemPedidoDTO> itemPedidoDTO;
    private UsuarioDTO usuarioDTO;
    private CupomDTO cupomAplicado;
    private EnderecoDTO enderecoEntrega;
}
