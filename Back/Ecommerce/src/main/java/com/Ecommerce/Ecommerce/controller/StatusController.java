package com.Ecommerce.Ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecommerce.Ecommerce.dto.StatusDTO;
import com.Ecommerce.Ecommerce.service.StatusService;

@RestController
@RequestMapping("/status")
public class StatusController {
    @Autowired
    private StatusService statusService;

    @PostMapping
    public ResponseEntity<StatusDTO> criarStatus(@RequestBody StatusDTO statusDTO) {
        StatusDTO novoStatus = statusService.criarStatus(statusDTO);
        return ResponseEntity.ok(novoStatus);
    }

    @GetMapping
    public ResponseEntity <List<StatusDTO>> obterTodosStatus() {
        List<StatusDTO> statusDTO = statusService.obterTodosStatus();
        return ResponseEntity.ok(statusDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusDTO> obterStatusPorId(@PathVariable Long id) {
        StatusDTO status = statusService.obterStatusPorId(id);
        return ResponseEntity.ok(status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusDTO> atualizarStatus(@PathVariable Long id, @RequestBody StatusDTO statusDTO) {
        StatusDTO statusAtualizado = statusService.atualizarStatus(id, statusDTO);
        return ResponseEntity.ok(statusAtualizado);
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/{id}")
    public Optional deletarStatus(@PathVariable Long id) {
        return Optional.ofNullable(statusService.deletarStatus(id));
    }
}
