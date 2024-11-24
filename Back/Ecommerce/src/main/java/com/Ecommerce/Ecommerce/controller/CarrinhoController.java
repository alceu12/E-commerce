package com.Ecommerce.Ecommerce.controller;

import com.Ecommerce.Ecommerce.dto.CarrinhoDTO;
import com.Ecommerce.Ecommerce.service.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService carrinhoService;

    @PostMapping("/adicionar-produto")
    public ResponseEntity<CarrinhoDTO> adicionarProduto(
            @RequestParam Long produtoId,
            @RequestParam int quantidade) {
        return ResponseEntity.ok(carrinhoService.adicionarProduto(produtoId, quantidade));
    }

    @DeleteMapping("/remover-produto/{produtoId}")
    public ResponseEntity<CarrinhoDTO> removerProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(carrinhoService.removerProduto(produtoId));
    }

    @DeleteMapping("/limpar")
    public ResponseEntity<CarrinhoDTO> limparCarrinho() {
        return ResponseEntity.ok(carrinhoService.limparCarrinho());
    }

    @GetMapping
    public ResponseEntity<CarrinhoDTO> obterCarrinho() {
        return ResponseEntity.ok(carrinhoService.obterCarrinho());
    }

    @PostMapping("/{carrinhoId}/alterar-quantidade")
    public ResponseEntity<CarrinhoDTO> alterarQuantidade(
            @PathVariable Long carrinhoId,
            @RequestParam Long produtoId,
            @RequestParam int quantidade) {
        return ResponseEntity.ok(carrinhoService.alterarQuantidade(carrinhoId, produtoId, quantidade));
    }

}
