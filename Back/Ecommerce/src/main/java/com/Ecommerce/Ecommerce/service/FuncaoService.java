package com.Ecommerce.Ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ecommerce.Ecommerce.dto.FuncaoDTO;
import com.Ecommerce.Ecommerce.entity.Funcao;
import com.Ecommerce.Ecommerce.repository.FuncaoRepository;
import com.Ecommerce.Ecommerce.util.FuncaoMapper;

@Service
public class FuncaoService {
    @Autowired
    private FuncaoRepository funcaoRepository;

    public FuncaoDTO criarFuncao(FuncaoDTO funcaoDTO) {
        Funcao funcao = FuncaoMapper.toEntity(funcaoDTO);
        funcao = funcaoRepository.save(funcao);
        return FuncaoMapper.toDTO(funcao);
    }

    public List<FuncaoDTO> obterTodosFuncao() {
        return funcaoRepository.findAll().stream()
                .map(FuncaoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public FuncaoDTO obterFuncaoPorId(Long id) {
        return funcaoRepository.findById(id)
                .map(FuncaoMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Funcao não encontrado"));
    }

    public FuncaoDTO atualizarFuncao(Long id, FuncaoDTO funcaoDTO) {
        Funcao funcaoExistente = funcaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcao não encontrado"));
        funcaoExistente.setNome(funcaoDTO.getNome());
        Funcao funcaoAtualizado = funcaoRepository.save(funcaoExistente);
        return FuncaoMapper.toDTO(funcaoAtualizado);
    }

    public boolean deletarFuncao(Long id) {
        Optional<Funcao> funcaoExistente = funcaoRepository.findById(id);
        if(funcaoExistente.isPresent()){
            funcaoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
