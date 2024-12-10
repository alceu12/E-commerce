package com.Ecommerce.Ecommerce.service;

import com.Ecommerce.Ecommerce.entity.Imagem;
import com.Ecommerce.Ecommerce.entity.Produto;
import com.Ecommerce.Ecommerce.repository.ProdutoRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceImagemTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produtoExistente;

    @BeforeEach
    void setUp() {
        produtoExistente = new Produto();
        produtoExistente.setId(1L);
        produtoExistente.setNome("Produto Teste");
        produtoExistente.setDescricao("Descrição Teste");
        produtoExistente.setValor(100.0);
        produtoExistente.setEstoque(10);
        produtoExistente.setImagens(new ArrayList<>());
    }

    @Test
    void adicionarImagemAoProduto_comProdutoExistente_deveAdicionarImagem() throws IOException {
        // Cenário
        byte[] dadosImagem = new byte[]{1, 2, 3}; // Simulação de bytes de uma imagem
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoExistente));
        when(multipartFile.getBytes()).thenReturn(dadosImagem);

        // Execução
        String resultado = produtoService.adicionarImagemAoProduto(1L, multipartFile);

        // Verificação
        verify(produtoRepository, times(1)).findById(1L);
        verify(produtoRepository, times(1)).save(any(Produto.class));
        Assertions.assertEquals("Imagem enviada com sucesso", resultado);
        Assertions.assertFalse(produtoExistente.getImagens().isEmpty());
        Imagem imagemAdicionada = produtoExistente.getImagens().get(0);
        Assertions.assertNotNull(imagemAdicionada.getDados());
        Assertions.assertArrayEquals(dadosImagem, imagemAdicionada.getDados());
        Assertions.assertEquals(produtoExistente, imagemAdicionada.getProduto());
    }

    @Test
    void adicionarImagemAoProduto_comProdutoInexistente_deveRetornarMensagemDeErro() throws IOException {
        // Cenário
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        // Execução
        String resultado = produtoService.adicionarImagemAoProduto(99L, multipartFile);

        // Verificação
        verify(produtoRepository, times(1)).findById(99L);
        verify(produtoRepository, never()).save(any(Produto.class));
        Assertions.assertEquals("Produto não encontrado", resultado);
    }

    @Test
    void adicionarImagemAoProduto_comIOException_deveRetornarMensagemDeErro() throws IOException {
        // Cenário
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoExistente));
        when(multipartFile.getBytes()).thenThrow(new IOException("Erro ao ler bytes do arquivo"));

        // Execução
        String resultado = produtoService.adicionarImagemAoProduto(1L, multipartFile);

        // Verificação
        verify(produtoRepository, times(1)).findById(1L);
        verify(produtoRepository, never()).save(any(Produto.class));
        Assertions.assertTrue(resultado.contains("Erro ao processar o arquivo de imagem"));
    }
}
