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

import com.Ecommerce.Ecommerce.dto.EnderecoDTO;
import com.Ecommerce.Ecommerce.service.EnderecoService;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {
    @Autowired
    private EnderecoService enderecoService;

    @PostMapping
    public ResponseEntity<EnderecoDTO> criarEndereco(@RequestBody EnderecoDTO enderecoDTO) {
        EnderecoDTO novoEndereco = enderecoService.criarEndereco(enderecoDTO);
        return ResponseEntity.ok(novoEndereco);
    }

    @GetMapping
    public ResponseEntity <List<EnderecoDTO>> obterTodosEndereco() {
        List<EnderecoDTO> enderecoDTO = enderecoService.obterTodosEndereco();
        return ResponseEntity.ok(enderecoDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoDTO> obterEnderecoPorId(@PathVariable Long id) {
        EnderecoDTO endereco = enderecoService.obterEnderecoPorId(id);
        return ResponseEntity.ok(endereco);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnderecoDTO> atualizarEndereco(@PathVariable Long id, @RequestBody EnderecoDTO enderecoDTO) {
        EnderecoDTO enderecoAtualizado = enderecoService.atualizarEndereco(id, enderecoDTO);
        return ResponseEntity.ok(enderecoAtualizado);
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/{id}")
    public Optional deletarEndereco(@PathVariable Long id) {
        return Optional.ofNullable(enderecoService.deletarEndereco(id));
    }
}