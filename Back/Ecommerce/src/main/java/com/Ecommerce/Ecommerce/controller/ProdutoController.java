package com.Ecommerce.Ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Ecommerce.Ecommerce.dto.ImagemDTO;
import com.Ecommerce.Ecommerce.dto.ProdutoDTO;
import com.Ecommerce.Ecommerce.service.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    public ProdutoDTO criarProduto(@RequestBody ProdutoDTO produtoDTO) {
        return produtoService.criarProduto(produtoDTO);

    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> obterTodosProdutos() {
        List<ProdutoDTO> produtos = produtoService.obterTodosProdutos();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> obterProdutoPorId(@PathVariable Long id) {
        ProdutoDTO produto = produtoService.obterProdutoPorId(id);
        return ResponseEntity.ok(produto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoDTO produtoDTO) {
        ProdutoDTO produtoAtualizado = produtoService.atualizarProduto(id, produtoDTO);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/{id}")
    public Optional deletarProduto(@PathVariable Long id) {
        return Optional.ofNullable(produtoService.deletarProduto(id));
    }

    @PostMapping("/{produtoId}/imagens")
    public ResponseEntity<String> uploadImagem(@PathVariable Long produtoId, @RequestParam("imagem") MultipartFile file) {
        String resultado = produtoService.adicionarImagemAoProduto(produtoId, file);
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }
}
