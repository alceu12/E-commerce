package com.Ecommerce.Ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ecommerce.Ecommerce.dto.CategoriaDTO;
import com.Ecommerce.Ecommerce.entity.Categoria;
import com.Ecommerce.Ecommerce.repository.CategoriaRepository;
import com.Ecommerce.Ecommerce.util.CategoriaMapper;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public CategoriaDTO criarCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria = CategoriaMapper.toEntity(categoriaDTO);
        categoria = categoriaRepository.save(categoria);
        return CategoriaMapper.toDTO(categoria);
    }

    public List<CategoriaDTO> obterTodosCategoria() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CategoriaDTO obterCategoriaPorId(Long id) {
        return categoriaRepository.findById(id)
                .map(CategoriaMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrado"));
    }

    public CategoriaDTO atualizarCategoria(Long id, CategoriaDTO categoriaDTO) {
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrado"));
        categoriaExistente.setNome(categoriaDTO.getNome());
        Categoria categoriaAtualizado = categoriaRepository.save(categoriaExistente);
        return CategoriaMapper.toDTO(categoriaAtualizado);
    }

    public boolean deletarCategoria(Long id) {
        Optional<Categoria> categoriaExistente = categoriaRepository.findById(id);
        if(categoriaExistente.isPresent()){
            categoriaRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
