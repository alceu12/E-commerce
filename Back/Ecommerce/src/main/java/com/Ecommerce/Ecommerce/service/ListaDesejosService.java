package com.Ecommerce.Ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.Ecommerce.Ecommerce.dto.ListaDesejosDTO;
import com.Ecommerce.Ecommerce.entity.ListaDesejos;
import com.Ecommerce.Ecommerce.entity.Produto;
import com.Ecommerce.Ecommerce.entity.Usuario;
import com.Ecommerce.Ecommerce.repository.ListaDesejosRepository;
import com.Ecommerce.Ecommerce.repository.ProdutoRepository;
import com.Ecommerce.Ecommerce.repository.UsuarioRepository;
import com.Ecommerce.Ecommerce.util.ListaDesejosMapper;

@Service
public class ListaDesejosService {
    @Autowired
    private ListaDesejosRepository listaDesejosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public ListaDesejosDTO criarListaDesejos(ListaDesejosDTO listaDesejosDTO) {
        // Converter DTO para entidade
        ListaDesejos listaDesejos = ListaDesejosMapper.toEntity(listaDesejosDTO);

        // Buscar e associar o usuario
        if (listaDesejosDTO.getUsuarioDTO() != null && listaDesejosDTO.getUsuarioDTO().getId() != null) {
            Optional<Usuario> usuarioOptional = usuarioRepository.findById(listaDesejosDTO.getUsuarioDTO().getId());
            if (usuarioOptional.isPresent()) {
                listaDesejos.setUsuario(usuarioOptional.get());
            } else {
                throw new RuntimeException("Usuario com ID " + listaDesejosDTO.getUsuarioDTO().getId() + " não encontrado.");
            }
        }

        // Salvar a lista de desejos
        listaDesejos = listaDesejosRepository.save(listaDesejos);
        return ListaDesejosMapper.toDTO(listaDesejos);
    }

    public ListaDesejosDTO adicionarProduto(Long listaDesejosId, Long produtoId) {
        ListaDesejos listaDesejos = listaDesejosRepository.findById(listaDesejosId)
                .orElseThrow(() -> new RuntimeException("Lista de Desejos não encontrada"));

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        listaDesejos.getProdutos().add(produto);
        listaDesejos = listaDesejosRepository.save(listaDesejos);

        return ListaDesejosMapper.toDTO(listaDesejos);
    }

    public List<ListaDesejosDTO> obterListasDesejosPorUsuario(Long usuarioId) {
        return listaDesejosRepository.findByUsuarioId(usuarioId).stream()
                .map(ListaDesejosMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ListaDesejosDTO obterListaDesejosPorId(Long id) {
        return listaDesejosRepository.findById(id)
                .map(ListaDesejosMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Lista de Desejos não encontrada"));
    }

    public ListaDesejosDTO atualizarListaDesejos(Long id, ListaDesejosDTO listaDesejosDTO) {
        ListaDesejos listaDesejosExistente = listaDesejosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista de Desejos não encontrada"));

        if (listaDesejosDTO.getUsuarioDTO() != null && listaDesejosDTO.getUsuarioDTO().getId() != null) {
            Optional<Usuario> usuarioOptional = usuarioRepository.findById(listaDesejosDTO.getUsuarioDTO().getId());
            usuarioOptional.ifPresent(listaDesejosExistente::setUsuario);
        }

        listaDesejosExistente.setProdutos(listaDesejosDTO.getProdutos().stream()
                .map(produtoDTO -> produtoRepository.findById(produtoDTO.getId())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado")))
                .collect(Collectors.toList()));

        ListaDesejos listaDesejosAtualizado = listaDesejosRepository.save(listaDesejosExistente);
        return ListaDesejosMapper.toDTO(listaDesejosAtualizado);
    }

    public boolean removerProduto(Long listaDesejosId, Long produtoId) {
        ListaDesejos listaDesejos = listaDesejosRepository.findById(listaDesejosId)
                .orElseThrow(() -> new RuntimeException("Lista de Desejos não encontrada"));

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        boolean removed = listaDesejos.getProdutos().remove(produto);

        if (removed) {
            listaDesejosRepository.save(listaDesejos);
        }

        return removed;
    }

    public boolean deletarListaDesejos(Long id) {
        Optional<ListaDesejos> listaDesejosExistente = listaDesejosRepository.findById(id);
        if (listaDesejosExistente.isPresent()) {
            listaDesejosRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
