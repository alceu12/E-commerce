package com.Ecommerce.Ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ecommerce.Ecommerce.dto.ProdutoDTO;
import com.Ecommerce.Ecommerce.entity.Categoria;
import com.Ecommerce.Ecommerce.entity.Produto;
import com.Ecommerce.Ecommerce.repository.CategoriaRepository;
import com.Ecommerce.Ecommerce.repository.ProdutoRepository;
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
        return produtoRepository.findAll().stream()
                .map(ProdutoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProdutoDTO obterProdutoPorId(Long id) {
        return produtoRepository.findById(id)
                .map(ProdutoMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public ProdutoDTO atualizarProduto(Long id, ProdutoDTO produtoDTO) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produtoExistente.setNome(produtoDTO.getNome());
        produtoExistente.setDescricao(produtoDTO.getDescricao());
        produtoExistente.setValor(produtoDTO.getValor());
        produtoExistente.setEstoque(produtoDTO.getEstoque());
        produtoExistente.setImagens(produtoDTO.getImagens());

        if (produtoDTO.getCategoriaDTO() != null && produtoDTO.getCategoriaDTO().getId() != null) {
            Optional<Categoria> categoriaOptional = categoriaRepository.findById(produtoDTO.getCategoriaDTO().getId());
            categoriaOptional.ifPresent(produtoExistente::setCategoria);
        }

        Produto produtoAtualizado = produtoRepository.save(produtoExistente);
        return ProdutoMapper.toDTO(produtoAtualizado);
    }

    public boolean deletarProduto(Long id) {
        Optional<Produto> produtoExistente = produtoRepository.findById(id);
        if(produtoExistente.isPresent()){
            produtoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
