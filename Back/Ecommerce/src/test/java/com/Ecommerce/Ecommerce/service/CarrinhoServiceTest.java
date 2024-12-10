package com.Ecommerce.Ecommerce.service;

import com.Ecommerce.Ecommerce.dto.CarrinhoDTO;
import com.Ecommerce.Ecommerce.entity.Carrinho;
import com.Ecommerce.Ecommerce.entity.ItemPedido;
import com.Ecommerce.Ecommerce.entity.Produto;
import com.Ecommerce.Ecommerce.repository.CarrinhoRepository;
import com.Ecommerce.Ecommerce.repository.ProdutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarrinhoServiceTest {

    @Mock
    private CarrinhoRepository carrinhoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private CarrinhoService carrinhoService;

    private Carrinho carrinho;
    private Produto produto;
    private ItemPedido itemPedido;

    @BeforeEach
    void setUp() {
        carrinho = new Carrinho();
        carrinho.setId(1L);
        carrinho.setItens(new ArrayList<>());

        produto = new Produto();
        produto.setId(10L);
        produto.setValor(50.0);
        produto.setEstoque(100);

        itemPedido = new ItemPedido();
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(2);
        itemPedido.setPrecoUnitario(50.0);
    }


    @Test
    void adicionarProduto_produtoNaoEncontrado() {
        when(carrinhoRepository.findAll()).thenReturn(List.of(carrinho));
        when(produtoRepository.findById(10L)).thenReturn(Optional.empty());

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            carrinhoService.adicionarProduto(10L, 5);
        });
        Assertions.assertTrue(thrown.getMessage().contains("Produto não encontrado"));
    }

    @Test
    void adicionarProduto_quantidadeZero() {
        when(carrinhoRepository.findAll()).thenReturn(List.of(carrinho));
        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            carrinhoService.adicionarProduto(10L, 0);
        });
        Assertions.assertTrue(thrown.getMessage().contains("Quantidade não pode ser zero"));
    }

    @Test
    void adicionarProduto_itemJaExiste_aumentaQuantidade() {
        carrinho.getItens().add(itemPedido);
        when(carrinhoRepository.findAll()).thenReturn(List.of(carrinho));
        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));
        when(carrinhoRepository.save(any(Carrinho.class))).thenReturn(carrinho);

        CarrinhoDTO resultado = carrinhoService.adicionarProduto(10L, 3);

        Assertions.assertEquals(5, carrinho.getItens().get(0).getQuantidade());
        Assertions.assertNotNull(resultado);
        verify(carrinhoRepository, times(1)).save(any(Carrinho.class));
    }

    @Test
    void adicionarProduto_quantidadeExcedeEstoque() {
        produto.setEstoque(3);
        when(carrinhoRepository.findAll()).thenReturn(List.of(carrinho));
        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            carrinhoService.adicionarProduto(10L, 4);
        });
        Assertions.assertTrue(thrown.getMessage().contains("Quantidade excede o estoque disponível"));
        verify(carrinhoRepository, never()).save(any(Carrinho.class));
    }

    @Test
    void removerProduto_sucesso() {
        carrinho.getItens().add(itemPedido);
        when(carrinhoRepository.findAll()).thenReturn(List.of(carrinho));
        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));
        when(carrinhoRepository.save(any(Carrinho.class))).thenReturn(carrinho);

        CarrinhoDTO resultado = carrinhoService.removerProduto(10L);
        Assertions.assertTrue(carrinho.getItens().isEmpty());
        Assertions.assertEquals(102, produto.getEstoque());
        Assertions.assertNotNull(resultado);
    }

    @Test
    void removerProduto_produtoNaoEncontradoNoCarrinho() {
        when(carrinhoRepository.findAll()).thenReturn(List.of(carrinho));
        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));
        when(carrinhoRepository.save(any(Carrinho.class))).thenReturn(carrinho);

        CarrinhoDTO resultado = carrinhoService.removerProduto(10L);
        Assertions.assertTrue(carrinho.getItens().isEmpty());
        Assertions.assertNotNull(resultado);
    }

    @Test
    void limparCarrinho_sucesso() {
        carrinho.getItens().add(itemPedido);
        when(carrinhoRepository.findAll()).thenReturn(List.of(carrinho));
        when(produtoRepository.findById(10L)).thenReturn(Optional.of(produto));
        when(carrinhoRepository.save(any(Carrinho.class))).thenReturn(carrinho);

        CarrinhoDTO resultado = carrinhoService.limparCarrinho();
        Assertions.assertTrue(carrinho.getItens().isEmpty());
        Assertions.assertEquals(102, produto.getEstoque());
        Assertions.assertNotNull(resultado);
    }

    @Test
    void obterCarrinho_jaExiste() {
        when(carrinhoRepository.findAll()).thenReturn(List.of(carrinho));

        CarrinhoDTO resultado = carrinhoService.obterCarrinho();
        Assertions.assertNotNull(resultado);
        verify(carrinhoRepository, never()).save(any(Carrinho.class));
    }

    @Test
    void obterCarrinho_naoExiste_criaNovo() {
        when(carrinhoRepository.findAll()).thenReturn(Collections.emptyList());
        when(carrinhoRepository.save(any(Carrinho.class))).thenAnswer(invocation -> {
            Carrinho c = invocation.getArgument(0);
            c.setId(2L);
            return c;
        });

        CarrinhoDTO resultado = carrinhoService.obterCarrinho();
        Assertions.assertNotNull(resultado);
        verify(carrinhoRepository, times(1)).save(any(Carrinho.class));
    }

    @Test
    void finalizarCarrinho_sucesso() {
        carrinho.getItens().add(itemPedido);
        when(carrinhoRepository.findAll()).thenReturn(List.of(carrinho));
        when(carrinhoRepository.save(any(Carrinho.class))).thenReturn(carrinho);

        CarrinhoDTO resultado = carrinhoService.finalizarCarrinho();
        Assertions.assertTrue(carrinho.getItens().isEmpty());
        Assertions.assertEquals(0.0, carrinho.getTotal());
        Assertions.assertNotNull(resultado);
    }

    @Test
    void alterarQuantidade_sucesso() {
        carrinho.setId(3L);
        carrinho.getItens().add(itemPedido);
        when(carrinhoRepository.findById(3L)).thenReturn(Optional.of(carrinho));
        when(carrinhoRepository.save(any(Carrinho.class))).thenReturn(carrinho);

        CarrinhoDTO resultado = carrinhoService.alterarQuantidade(3L, 10L, 5);
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(5, carrinho.getItens().get(0).getQuantidade());
    }

    @Test
    void alterarQuantidade_carrinhoNaoEncontrado() {
        when(carrinhoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            carrinhoService.alterarQuantidade(99L, 10L, 5);
        });
        Assertions.assertTrue(thrown.getMessage().contains("Carrinho não encontrado"));
        verify(carrinhoRepository, never()).save(any(Carrinho.class));
    }

    @Test
    void alterarQuantidade_itemNaoEncontrado() {
        carrinho.setId(3L);
        when(carrinhoRepository.findById(3L)).thenReturn(Optional.of(carrinho));
        when(carrinhoRepository.save(any(Carrinho.class))).thenReturn(carrinho);

        CarrinhoDTO resultado = carrinhoService.alterarQuantidade(3L, 10L, 5);
        Assertions.assertNotNull(resultado);
        Assertions.assertTrue(carrinho.getItens().isEmpty());
    }
}
