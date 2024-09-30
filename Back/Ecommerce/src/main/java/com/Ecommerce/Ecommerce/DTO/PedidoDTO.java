package com.Ecommerce.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {
    private Long id;
    private double total;
    private StatusDTO statusDTO;
    private ItemPedidoDTO itemPedidoDTO;
    private UsuarioDTO usuarioDTO;
}
