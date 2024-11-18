package com.Ecommerce.Ecommerce.service;

import com.Ecommerce.Ecommerce.entity.Carrinho;
import com.Ecommerce.Ecommerce.entity.ItemPedido;
import com.Ecommerce.Ecommerce.entity.Produto;
import com.Ecommerce.Ecommerce.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

@Service
public class CarrinhoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Cacheable(value = "carrinho", key = "#usuarioId")
    public Carrinho getCarrinho(Long usuarioId) {
        // Se não houver carrinho no cache, cria um novo
        return new Carrinho(usuarioId);
    }

    @CachePut(value = "carrinho", key = "#usuarioId")
    public Carrinho adicionarItem(Long usuarioId, ItemPedido itemPedido) {
        Carrinho carrinho = getCarrinho(usuarioId);

        // Obter o produto do banco de dados
        Produto produto = produtoRepository.findById(itemPedido.getProduto().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        // Definir o preço unitário do produto
        itemPedido.setPrecoUnitario(produto.getValor());
        itemPedido.setProduto(produto);

        // Adicionar o item ao carrinho
        carrinho.adicionarItem(itemPedido);

        return carrinho;
    }

    @CachePut(value = "carrinho", key = "#usuarioId")
    public Carrinho removerItem(Long usuarioId, Long produtoId) {
        Carrinho carrinho = getCarrinho(usuarioId);
        carrinho.removerItem(produtoId);
        return carrinho;
    }


    @CacheEvict(value = "carrinho", key = "#usuarioId")
    public void limparCarrinho(Long usuarioId) {
        // Remove o carrinho do cache
    }
}
