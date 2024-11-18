package com.Ecommerce.Ecommerce.controller;

import com.Ecommerce.Ecommerce.entity.Carrinho;
import com.Ecommerce.Ecommerce.entity.ItemPedido;
import com.Ecommerce.Ecommerce.service.CarrinhoService;
import com.Ecommerce.Ecommerce.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService carrinhoService;

    @Autowired
    private ProdutoService produtoService;

    // Obter o carrinho do usuário
    @GetMapping("/{usuarioId}")
    public Carrinho getCarrinho(@PathVariable Long usuarioId) {
        return carrinhoService.getCarrinho(usuarioId);
    }

    // Adicionar um item ao carrinho
    @PostMapping("/{usuarioId}/adicionar")
    public Carrinho adicionarItem(@PathVariable Long usuarioId, @RequestBody ItemPedido itemPedido) {
        // Opcional: Validar se o produto existe e ajustar o preço unitário
        return carrinhoService.adicionarItem(usuarioId, itemPedido);
    }

    // Remover um item do carrinho
    @DeleteMapping("/{usuarioId}/remover/{produtoId}")
    public Carrinho removerItem(@PathVariable Long usuarioId, @PathVariable Long produtoId) {
        return carrinhoService.removerItem(usuarioId, produtoId);
    }


    // Limpar o carrinho
    @DeleteMapping("/{usuarioId}/limpar")
    public void limparCarrinho(@PathVariable Long usuarioId) {
        carrinhoService.limparCarrinho(usuarioId);
    }
}
