package com.Ecommerce.Ecommerce.controller;

import com.Ecommerce.Ecommerce.dto.ListaDesejosDTO;
import com.Ecommerce.Ecommerce.service.ListaDesejosService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lista-desejos")
public class ListaDesejosController {

    @Autowired
    private ListaDesejosService listaDesejosService;

    @PostMapping
    public ListaDesejosDTO criarListaDesejos(@RequestBody ListaDesejosDTO listaDesejosDTO) {
        return listaDesejosService.criarListaDesejos(listaDesejosDTO);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ListaDesejosDTO>> obterListasDesejosPorUsuario(@PathVariable Long usuarioId) {
        List<ListaDesejosDTO> listasDesejos = listaDesejosService.obterListasDesejosPorUsuario(usuarioId);
        return ResponseEntity.ok(listasDesejos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaDesejosDTO> obterListaDesejosPorId(@PathVariable Long id) {
        ListaDesejosDTO listaDesejos = listaDesejosService.obterListaDesejosPorId(id);
        return ResponseEntity.ok(listaDesejos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListaDesejosDTO> atualizarListaDesejos(@PathVariable Long id, @RequestBody ListaDesejosDTO listaDesejosDTO) {
        ListaDesejosDTO listaDesejosAtualizada = listaDesejosService.atualizarListaDesejos(id, listaDesejosDTO);
        return ResponseEntity.ok(listaDesejosAtualizada);
    }

    @PostMapping("/{listaDesejosId}/produtos/{produtoId}")
    public ResponseEntity<ListaDesejosDTO> adicionarProduto(@PathVariable Long listaDesejosId, @PathVariable Long produtoId) {
        ListaDesejosDTO listaDesejosAtualizada = listaDesejosService.adicionarProduto(listaDesejosId, produtoId);
        return ResponseEntity.ok(listaDesejosAtualizada);
    }

    @DeleteMapping("/{listaDesejosId}/produtos/{produtoId}")
    public ResponseEntity<Boolean> removerProduto(@PathVariable Long listaDesejosId, @PathVariable Long produtoId) {
        boolean removido = listaDesejosService.removerProduto(listaDesejosId, produtoId);
        return ResponseEntity.ok(removido);
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/{id}")
    public Optional deletarListaDesejos(@PathVariable Long id) {
        return Optional.ofNullable(listaDesejosService.deletarListaDesejos(id));
    }
}