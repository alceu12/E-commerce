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

        // Verificar se a quantidade é zero
        if (quantidade == 0) {
            throw new RuntimeException("Quantidade não pode ser zero");
        }

        // Verificar se o item já está no carrinho
        Optional<ItemPedido> itemExistente = carrinho.getItens().stream()
                .filter(item -> item.getProduto().getId().equals(produtoId))
                .findFirst();

        int estoqueAtual = produto.getEstoque();

        if (itemExistente.isPresent()) {
            ItemPedido item = itemExistente.get();
            int novaQuantidade = item.getQuantidade() + quantidade;

            // Verificar se a nova quantidade é válida
            if (novaQuantidade < 0) {
                throw new RuntimeException("Quantidade no carrinho não pode ser negativa");
            }

            if (quantidade > estoqueAtual) {
                throw new RuntimeException("Quantidade excede o estoque disponível");
            }

            // Atualizar ou remover o item do carrinho
            if (novaQuantidade == 0) {
                carrinho.getItens().remove(item); // Remove o item se a quantidade for zero
            } else {
                item.setQuantidade(novaQuantidade);
            }
        } else {
            if (quantidade < 0) {
                throw new RuntimeException("Não é possível remover um produto que não está no carrinho");
            }

            if (quantidade > estoqueAtual) {
                throw new RuntimeException("Quantidade excede o estoque disponível");
            }

            // Criar um novo item de pedido
            ItemPedido novoItem = new ItemPedido();
            novoItem.setProduto(produto);
            novoItem.setQuantidade(quantidade);
            novoItem.setPrecoUnitario(produto.getValor());

            // Adicionar o novo item ao carrinho
            carrinho.getItens().add(novoItem);
        }

        // Atualizar o estoque do produto corretamente
        int novoEstoque = estoqueAtual - quantidade;
        if (novoEstoque < 0) {
            throw new RuntimeException("Erro: Estoque inconsistente após a atualização");
        }
        produto.setEstoque(novoEstoque);
        produtoRepository.save(produto);

        // Atualizar o total do carrinho
        carrinho.atualizarTotal();
        carrinhoRepository.save(carrinho);

        return CarrinhoMapper.toDTO(carrinho);
    }




    public CarrinhoDTO removerProduto(Long produtoId) {
        Carrinho carrinho = carrinhoRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        carrinho.getItens().stream()
                .filter(item -> item.getProduto().getId().equals(produtoId))
                .findFirst()
                .ifPresent(item -> {
                    Produto produto = produtoRepository.findById(produtoId)
                            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
                    produto.setEstoque(produto.getEstoque() + item.getQuantidade());
                    produtoRepository.save(produto);
                });
        carrinho.getItens().removeIf(item -> item.getProduto().getId().equals(produtoId));
        carrinho.atualizarTotal();
        carrinhoRepository.save(carrinho);

        // tem q pegar a quantidade do item removido e adicionar ao estoque
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        // quantidade do item removido


        produtoRepository.save(produto);


        return CarrinhoMapper.toDTO(carrinho);
    }

    public CarrinhoDTO limparCarrinho() {
        Carrinho carrinho = carrinhoRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        carrinho.getItens().forEach(item -> {
            Produto produto = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
            produto.setEstoque(produto.getEstoque() + item.getQuantidade());
            produtoRepository.save(produto);
        });


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

    public CarrinhoDTO finalizarCarrinho() {
        Carrinho carrinho = carrinhoRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        carrinho.getItens().clear();
        carrinho.setTotal(0.0);
        carrinhoRepository.save(carrinho);

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
