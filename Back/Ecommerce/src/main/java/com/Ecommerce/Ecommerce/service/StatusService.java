package com.Ecommerce.Ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ecommerce.Ecommerce.dto.StatusDTO;
import com.Ecommerce.Ecommerce.entity.Status;
import com.Ecommerce.Ecommerce.repository.StatusRepository;
import com.Ecommerce.Ecommerce.util.StatusMapper;

@Service
public class StatusService {
    @Autowired
    private StatusRepository statusRepository;

    public StatusDTO criarStatus(StatusDTO statusDTO) {
        Status status = StatusMapper.toEntity(statusDTO);
        status = statusRepository.save(status);
        return StatusMapper.toDTO(status);
    }

    public List<StatusDTO> obterTodosStatus() {
        return statusRepository.findAll().stream()
                .map(StatusMapper::toDTO)
                .collect(Collectors.toList());
    }

    public StatusDTO obterStatusPorId(Long id) {
        return statusRepository.findById(id)
                .map(StatusMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Status não encontrado"));
    }

    public StatusDTO atualizarStatus(Long id, StatusDTO statusDTO) {
        Status statusExistente = statusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Status não encontrado"));
        statusExistente.setNome(statusDTO.getNome());
        statusExistente.setCodigo(statusDTO.getCodigo());
        Status statusAtualizado = statusRepository.save(statusExistente);
        return StatusMapper.toDTO(statusAtualizado);
    }

    public boolean deletarStatus(Long id) {
        Optional<Status> statusExistente = statusRepository.findById(id);
        if(statusExistente.isPresent()){
            statusRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
