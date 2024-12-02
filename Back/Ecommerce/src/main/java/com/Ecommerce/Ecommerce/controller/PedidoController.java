package com.Ecommerce.Ecommerce.controller;

import java.util.Optional;
import java.util.List;

import com.Ecommerce.Ecommerce.dto.UpdateStatusDTO;
import com.Ecommerce.Ecommerce.entity.Pedido;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Ecommerce.Ecommerce.dto.PedidoDTO;
import com.Ecommerce.Ecommerce.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public PedidoDTO criarPedido(@RequestBody PedidoDTO pedidoDTO) {
        return pedidoService.criarPedido(pedidoDTO);

    }

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> obterTodosPedidos() {
        List<PedidoDTO> pedidos = pedidoService.obterTodosPedidos();
        return ResponseEntity.ok(pedidos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> obterPedidoPorId(@PathVariable Long id) {
        PedidoDTO pedido = pedidoService.obterPedidoPorId(id);
        return ResponseEntity.ok(pedido);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoDTO> atualizarPedido(@PathVariable Long id, @RequestBody PedidoDTO pedidoDTO) {
        PedidoDTO pedidoAtualizado = pedidoService.atualizarPedido(id, pedidoDTO);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/{id}")
    public Optional deletarPedido(@PathVariable Long id) {
        return Optional.ofNullable(pedidoService.deletarPedido(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoDTO> atualizarStatusPedido(
            @PathVariable Long id,
            @RequestBody UpdateStatusDTO updateStatusDTO) {
        try {
            PedidoDTO pedidoAtualizado = pedidoService.atualizarStatusPedido(id, updateStatusDTO.getStatusPedido());
            return ResponseEntity.ok(pedidoAtualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<PedidoDTO>> obterPedidosPorUsuario(@PathVariable Long id) {
        List<PedidoDTO> pedidos = pedidoService.obterPedidosPorUsuario(id);
        return ResponseEntity.ok(pedidos);
    }
}
