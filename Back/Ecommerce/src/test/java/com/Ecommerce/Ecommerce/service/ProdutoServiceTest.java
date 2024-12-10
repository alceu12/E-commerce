package com.Ecommerce.Ecommerce.service;

import com.Ecommerce.Ecommerce.dto.CategoriaDTO;
import com.Ecommerce.Ecommerce.dto.ProdutoDTO;
import com.Ecommerce.Ecommerce.entity.Categoria;
import com.Ecommerce.Ecommerce.entity.Produto;
import com.Ecommerce.Ecommerce.repository.CategoriaRepository;
import com.Ecommerce.Ecommerce.repository.ProdutoRepository;
import com.Ecommerce.Ecommerce.util.ProdutoMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private ProdutoDTO produtoDTO;
    private Produto produto;
    private Categoria categoria;
    private CategoriaDTO categoriaDTO;
    private Produto produtoSemCategoria;
    private Produto produtoComCatId;

    @BeforeEach
    void setUp() {
        // Objeto base do ProdutoDTO (sem categoria)
        produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("Produto Teste");
        produtoDTO.setDescricao("Descrição Teste");
        produtoDTO.setValor(99.99);
        produtoDTO.setEstoque(10);

        // Objeto base do Produto (entidade)
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setDescricao("Descrição Teste");
        produto.setValor(99.99);
        produto.setEstoque(10);

        // Produto sem categoria (simulado do toEntity)
        produtoSemCategoria = new Produto();
        produtoSemCategoria.setNome("Produto Teste");
        produtoSemCategoria.setDescricao("Descrição Teste");
        produtoSemCategoria.setValor(99.99);
        produtoSemCategoria.setEstoque(10);

        // Categoria e CategoriaDTO para testes com categoria
        categoria = new Categoria();
        categoria.setId(2L);
        categoria.setNome("Categoria Teste");

        categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(2L);
        categoriaDTO.setNome("Categoria Teste");

        // Produto com ID de categoria (simulado do toEntity quando DTO tem categoria)
        produtoComCatId = new Produto();
        produtoComCatId.setNome("Produto Teste");
        produtoComCatId.setDescricao("Descrição Teste");
        produtoComCatId.setValor(99.99);
        produtoComCatId.setEstoque(10);
        Categoria catApenasId = new Categoria();
        catApenasId.setId(2L);
        produtoComCatId.setCategoria(catApenasId);
    }

    @Test
    void criarProdutoSemCategoria_deveSalvarEReornarProdutoDTO() {
        try (MockedStatic<ProdutoMapper> mocked = mockStatic(ProdutoMapper.class)) {
            // Configurar o mock static para o mapper
            mocked.when(() -> ProdutoMapper.toEntity(produtoDTO)).thenReturn(produtoSemCategoria);
            mocked.when(() -> ProdutoMapper.toDTO(any(Produto.class))).thenReturn(produtoDTO);

            when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

            // Execução
            ProdutoDTO resultado = produtoService.criarProduto(produtoDTO);

            // Verificações
            verify(produtoRepository, times(1)).save(any(Produto.class));
            Assertions.assertNotNull(resultado);
            Assertions.assertEquals("Produto Teste", resultado.getNome());
            Assertions.assertNull(resultado.getCategoriaDTO());
        }
        // Ao final do bloco try-with-resources, o mock static é finalizado.
    }

    @Test
    void criarProdutoComCategoriaExistente_deveAssociarCategoriaESalvar() {
        produtoDTO.setCategoriaDTO(categoriaDTO);

        try (MockedStatic<ProdutoMapper> mocked = mockStatic(ProdutoMapper.class)) {
            // Configurar o mock static para o mapper
            mocked.when(() -> ProdutoMapper.toEntity(produtoDTO)).thenReturn(produtoComCatId);
            mocked.when(() -> ProdutoMapper.toDTO(any(Produto.class))).thenReturn(produtoDTO);

            when(categoriaRepository.findById(2L)).thenReturn(Optional.of(categoria));
            when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

            // Execução
            ProdutoDTO resultado = produtoService.criarProduto(produtoDTO);

            // Verificações
            verify(categoriaRepository, times(1)).findById(2L);
            verify(produtoRepository, times(1)).save(any(Produto.class));
            Assertions.assertNotNull(resultado);
            Assertions.assertEquals("Produto Teste", resultado.getNome());
            Assertions.assertNotNull(resultado.getCategoriaDTO());
            Assertions.assertEquals(2L, resultado.getCategoriaDTO().getId());
        }
    }

    @Test
    void criarProdutoComCategoriaInexistente_deveLancarExcecao() {
        produtoDTO.setCategoriaDTO(categoriaDTO);

        try (MockedStatic<ProdutoMapper> mocked = mockStatic(ProdutoMapper.class)) {
            mocked.when(() -> ProdutoMapper.toEntity(produtoDTO)).thenReturn(produtoComCatId);

            // Categoria não encontrada
            when(categoriaRepository.findById(2L)).thenReturn(Optional.empty());

            // Execução & verificação da exceção
            RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
                produtoService.criarProduto(produtoDTO);
            });

            Assertions.assertEquals("Categoria com ID 2 não encontrado.", thrown.getMessage());
            verify(produtoRepository, never()).save(any(Produto.class));
        }
    }
}
