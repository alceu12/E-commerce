package com.Ecommerce.Ecommerce.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Ecommerce.Ecommerce.dto.ProdutoDTO;
import com.Ecommerce.Ecommerce.entity.Categoria;
import com.Ecommerce.Ecommerce.entity.Imagem;
import com.Ecommerce.Ecommerce.entity.Produto;
import com.Ecommerce.Ecommerce.repository.CategoriaRepository;
import com.Ecommerce.Ecommerce.repository.ProdutoRepository;
import com.Ecommerce.Ecommerce.util.ImagemMapper;
import com.Ecommerce.Ecommerce.util.ProdutoMapper;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public ProdutoDTO criarProduto(ProdutoDTO produtoDTO) {
        // Converter DTO para entidade produto
        Produto produto = ProdutoMapper.toEntity(produtoDTO);

        // Buscar e associar o categoria usando o ID
        if (produto.getCategoria() != null && produto.getCategoria().getId() != null) {
            Optional<Categoria> categoriaOptional = categoriaRepository.findById(produto.getCategoria().getId());
            if (categoriaOptional.isPresent()) {
                produto.setCategoria(categoriaOptional.get());
            } else {
                throw new RuntimeException("Categoria com ID " + produto.getCategoria().getId() + " não encontrado.");
            }
        }

        produto = produtoRepository.save(produto);
        return ProdutoMapper.toDTO(produto);
    }

    public List<ProdutoDTO> obterTodosProdutos() {
        return produtoRepository.findAll().stream().map(ProdutoMapper::toDTO).collect(Collectors.toList());
    }

    public List<ProdutoDTO> obterProdutosPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome).stream().map(ProdutoMapper::toDTO).collect(Collectors.toList());
    }

    public List<ProdutoDTO> filtrarProdutos(List<String> categorias, Double precoMin, Double precoMax) {
        List<Produto> produtosFiltrados = new ArrayList<>();

        List<Produto> todosProdutos = produtoRepository.findAll();

        for (Produto produto : todosProdutos) {
            boolean matches = true;

            // Verificar estoque maior que zero
            if (produto.getEstoque() <= 0) {
                matches = false;
            }

            // Filtrar por categorias
            if (matches && categorias != null && !categorias.isEmpty()) {
                if (produto.getCategoria() == null || !categorias.contains(produto.getCategoria().getNome())) {
                    matches = false;
                }
            }

            // Filtrar por preço mínimo
            if (matches && precoMin != null) {
                if (produto.getValor() < precoMin) {
                    matches = false;
                }
            }

            // Filtrar por preço máximo
            if (matches && precoMax != null) {
                if (produto.getValor() > precoMax) {
                    matches = false;
                }
            }

            if (matches) {
                produtosFiltrados.add(produto);
            }
        }

        // Converter para ProdutoDTO
        List<ProdutoDTO> produtosDTO = new ArrayList<>();
        for (Produto produto : produtosFiltrados) {
            produtosDTO.add(ProdutoMapper.toDTO(produto));
        }

        return produtosDTO;
    }

    public List<ProdutoDTO> obterProdutosPorCategoria(Long categoriaId, Long produtoId) {
        return produtoRepository.findAll((Specification<Produto>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (categoriaId != null) {
                Join<Produto, Categoria> join = root.join("categoria");
                predicates.add(criteriaBuilder.equal(join.get("id"), categoriaId));
            }
            // estoque zerado n pegar
            predicates.add(criteriaBuilder.greaterThan(root.get("estoque"), 0));

            if (produtoId != null) {
                predicates.add(criteriaBuilder.notEqual(root.get("id"), produtoId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }).stream().map(ProdutoMapper::toDTO).collect(Collectors.toList());
    }


    public ProdutoDTO obterProdutoPorId(Long id) {
        return produtoRepository.findById(id).map(ProdutoMapper::toDTO).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public ProdutoDTO atualizarProduto(Long id, ProdutoDTO produtoDTO) {
        Produto produtoExistente = produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produtoExistente.setNome(produtoDTO.getNome());
        produtoExistente.setDescricao(produtoDTO.getDescricao());
        produtoExistente.setValor(produtoDTO.getValor());
        produtoExistente.setEstoque(produtoDTO.getEstoque());

        if (produtoDTO.getCategoriaDTO() != null && produtoDTO.getCategoriaDTO().getId() != null) {
            Categoria categoria = categoriaRepository.findById(produtoDTO.getCategoriaDTO().getId()).orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
            produtoExistente.setCategoria(categoria);
        }

        // Atualiza as imagens apenas se forem fornecidas
        if (produtoDTO.getImagens() != null && !produtoDTO.getImagens().isEmpty()) {
            List<Imagem> imagens = produtoDTO.getImagens().stream().map(imagemDTO -> ImagemMapper.toEntity(imagemDTO, produtoExistente)) // Passando o produto
                    .collect(Collectors.toList());
            produtoExistente.setImagens(imagens);
        }

        Produto produtoAtualizado = produtoRepository.save(produtoExistente);
        return ProdutoMapper.toDTO(produtoAtualizado);
    }


    public boolean deletarProduto(Long id) {
        Optional<Produto> produtoExistente = produtoRepository.findById(id);
        if (produtoExistente.isPresent()) {
            produtoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public String adicionarImagemAoProduto(Long produtoId, MultipartFile file) {
        try {
            Produto produto = produtoRepository.findById(produtoId).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            Imagem imagem = new Imagem();
            imagem.setDados(file.getBytes());
            imagem.setProduto(produto);

            produto.getImagens().add(imagem);
            produtoRepository.save(produto);

            return "Imagem enviada com sucesso";
        } catch (IOException e) {
            return "Erro ao processar o arquivo de imagem: " + e.getMessage();
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
