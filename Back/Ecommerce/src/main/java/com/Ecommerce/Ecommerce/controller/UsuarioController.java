package com.Ecommerce.Ecommerce.controller;

import java.util.List;
import java.util.Optional;

import com.Ecommerce.Ecommerce.dto.EnderecoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Ecommerce.Ecommerce.dto.UsuarioDTO;
import com.Ecommerce.Ecommerce.service.UsuarioService;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> criarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.criarUsuario(usuarioDTO);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obterTodosUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.obterTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obterUsuarioPorId(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.obterUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDTO);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/{id}")
    public Optional deletarUsuario(@PathVariable Long id) {
        return Optional.ofNullable(usuarioService.deletarUsuario(id));
    }

    @PutMapping("/{userId}/endereco")
    public ResponseEntity<?> atualizarEndereco(@PathVariable Long userId, @RequestBody EnderecoDTO enderecoDTO) {
        try {
            String mensagem = usuarioService.atualizarEndereco(userId, enderecoDTO);
            return ResponseEntity.ok(mensagem);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    @GetMapping("/{username}/verify")
    public ResponseEntity<Boolean> verifyIfUserExists(@PathVariable String username) {
        return ResponseEntity.ok(usuarioService.verifyIfUserExists(username));
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = usuarioService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
}
