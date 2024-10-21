package com.Ecommerce.Ecommerce.dto;

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
    private List<ItemPedidoDTO> itemPedidoDTO;
    private UsuarioDTO usuarioDTO;
    
}
