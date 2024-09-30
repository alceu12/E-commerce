package com.Ecommerce.Ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecommerce.Ecommerce.dto.ItemPedidoDTO;
import com.Ecommerce.Ecommerce.service.ItemPedidoService;

@RestController
@RequestMapping("/itempedido")
public class ItemPedidoController {
    @Autowired
    private ItemPedidoService itemPedidoService;

    @PostMapping
    public ItemPedidoDTO criarItemPedido(@RequestBody ItemPedidoDTO itemPedidoDTO) {
        return itemPedidoService.criarItemPedido(itemPedidoDTO);

    }

    @GetMapping
    public ResponseEntity<List<ItemPedidoDTO>> obterTodosItemPedidos() {
        List<ItemPedidoDTO> itemPedidos = itemPedidoService.obterTodosItemPedidos();
        return ResponseEntity.ok(itemPedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemPedidoDTO> obterItemPedidoPorId(@PathVariable Long id) {
        ItemPedidoDTO itemPedido = itemPedidoService.obterItemPedidoPorId(id);
        return ResponseEntity.ok(itemPedido);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemPedidoDTO> atualizarItemPedido(@PathVariable Long id, @RequestBody ItemPedidoDTO itemPedidoDTO) {
        ItemPedidoDTO itemPedidoAtualizado = itemPedidoService.atualizarItemPedido(id, itemPedidoDTO);
        return ResponseEntity.ok(itemPedidoAtualizado);
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/{id}")
    public Optional deletarItemPedido(@PathVariable Long id) {
        return Optional.ofNullable(itemPedidoService.deletarItemPedido(id));
    }
}
