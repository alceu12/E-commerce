package com.Ecommerce.Ecommerce.service;

import com.Ecommerce.Ecommerce.dto.CarrinhoDTO;
import com.Ecommerce.Ecommerce.entity.Carrinho;
import com.Ecommerce.Ecommerce.entity.ItemPedido;
import com.Ecommerce.Ecommerce.entity.Produto;
import com.Ecommerce.Ecommerce.repository.CarrinhoRepository;
import com.Ecommerce.Ecommerce.repository.ProdutoRepository;
import com.Ecommerce.Ecommerce.util.CarrinhoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarrinhoService {

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public CarrinhoDTO adicionarProduto(Long produtoId, int quantidade) {
        // Buscar ou criar o carrinho
        Carrinho carrinho = carrinhoRepository.findAll().stream()
                .findFirst() // Considera que há um único carrinho livre no sistema
                .orElseGet(() -> carrinhoRepository.save(new Carrinho()));

        // Buscar o produto
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // Verificar se o item já está no carrinho
        Optional<ItemPedido> itemExistente = carrinho.getItens().stream()
                .filter(item -> item.getProduto().getId().equals(produtoId))
                .findFirst();

        if (itemExistente.isPresent()) {
            ItemPedido item = itemExistente.get();
            item.setQuantidade(item.getQuantidade() + quantidade);
        } else {
            ItemPedido novoItem = new ItemPedido();
            novoItem.setProduto(produto);
            novoItem.setQuantidade(quantidade);
            novoItem.setPrecoUnitario(produto.getValor());

            carrinho.getItens().add(novoItem);
        }

        // Atualizar o total
        carrinho.atualizarTotal();
        carrinhoRepository.save(carrinho);

        return CarrinhoMapper.toDTO(carrinho);
    }

    public CarrinhoDTO removerProduto(Long produtoId) {
        Carrinho carrinho = carrinhoRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        carrinho.getItens().removeIf(item -> item.getProduto().getId().equals(produtoId));
        carrinho.atualizarTotal();
        carrinhoRepository.save(carrinho);

        return CarrinhoMapper.toDTO(carrinho);
    }

    public CarrinhoDTO limparCarrinho() {
        Carrinho carrinho = carrinhoRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        carrinho.getItens().clear();
        carrinho.setTotal(0.0);
        carrinhoRepository.save(carrinho);

        return CarrinhoMapper.toDTO(carrinho);
    }

    public CarrinhoDTO obterCarrinho() {
        Carrinho carrinho = carrinhoRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> carrinhoRepository.save(new Carrinho()));

        return CarrinhoMapper.toDTO(carrinho);
    }
    public CarrinhoDTO alterarQuantidade(Long carrinhoId, Long produtoId, int quantidade) {
        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        carrinho.getItens().stream()
                .filter(item -> item.getProduto().getId().equals(produtoId))
                .findFirst()
                .ifPresent(item -> item.setQuantidade(quantidade));

        carrinho.atualizarTotal();
        carrinhoRepository.save(carrinho);

        return CarrinhoMapper.toDTO(carrinho);
    }

}
