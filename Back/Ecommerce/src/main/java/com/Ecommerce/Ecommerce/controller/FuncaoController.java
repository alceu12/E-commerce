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

import com.Ecommerce.Ecommerce.dto.FuncaoDTO;
import com.Ecommerce.Ecommerce.service.FuncaoService;

@RestController
@RequestMapping("/funcoes")
public class FuncaoController {
    @Autowired
    private FuncaoService funcaoService;

    @PostMapping
    public ResponseEntity<FuncaoDTO> criarFuncao(@RequestBody FuncaoDTO funcaoDTO) {
        FuncaoDTO novoFuncao = funcaoService.criarFuncao(funcaoDTO);
        return ResponseEntity.ok(novoFuncao);
    }

    @GetMapping
    public ResponseEntity <List<FuncaoDTO>> obterTodosFuncao() {
        List<FuncaoDTO> funcaoDTO = funcaoService.obterTodosFuncao();
        return ResponseEntity.ok(funcaoDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncaoDTO> obterFuncaoPorId(@PathVariable Long id) {
        FuncaoDTO funcao = funcaoService.obterFuncaoPorId(id);
        return ResponseEntity.ok(funcao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncaoDTO> atualizarFuncao(@PathVariable Long id, @RequestBody FuncaoDTO funcaoDTO) {
        FuncaoDTO funcaoAtualizado = funcaoService.atualizarFuncao(id, funcaoDTO);
        return ResponseEntity.ok(funcaoAtualizado);
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/{id}")
    public Optional deletarFuncao(@PathVariable Long id) {
        return Optional.ofNullable(funcaoService.deletarFuncao(id));
    }
}
