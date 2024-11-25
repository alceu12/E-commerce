package com.Ecommerce.Ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ecommerce.Ecommerce.dto.EnderecoDTO;
import com.Ecommerce.Ecommerce.entity.Endereco;
import com.Ecommerce.Ecommerce.repository.EnderecoRepository;
import com.Ecommerce.Ecommerce.util.EnderecoMapper;

@Service
public class EnderecoService {
    @Autowired
    private EnderecoRepository enderecoRepository;

    public EnderecoDTO criarEndereco(EnderecoDTO enderecoDTO) {
        Endereco endereco = EnderecoMapper.toEntity(enderecoDTO);
        endereco = enderecoRepository.save(endereco);
        return EnderecoMapper.toDTO(endereco);
    }

    public List<EnderecoDTO> obterTodosEndereco() {
        return enderecoRepository.findAll().stream()
                .map(EnderecoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public EnderecoDTO obterEnderecoPorId(Long id) {
        return enderecoRepository.findById(id)
                .map(EnderecoMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Endereco não encontrado"));
    }

    public EnderecoDTO atualizarEndereco(Long id, EnderecoDTO enderecoDTO) {
        Endereco enderecoExistente = enderecoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereco não encontrado"));
        enderecoExistente.setCep(enderecoDTO.getCep());
        enderecoExistente.setRua(enderecoDTO.getRua());
        enderecoExistente.setNumero(enderecoDTO.getNumero());
        enderecoExistente.setComplemento(enderecoDTO.getComplemento());
        Endereco enderecoAtualizado = enderecoRepository.save(enderecoExistente);
        return EnderecoMapper.toDTO(enderecoAtualizado);
    }

    public boolean deletarEndereco(Long id) {
        Optional<Endereco> enderecoExistente = enderecoRepository.findById(id);
        if (enderecoExistente.isPresent()) {
            enderecoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
