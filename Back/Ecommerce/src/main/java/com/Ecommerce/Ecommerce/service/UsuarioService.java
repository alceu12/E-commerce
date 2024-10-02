package com.Ecommerce.Ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Ecommerce.Ecommerce.dto.UsuarioDTO;
import com.Ecommerce.Ecommerce.entity.Endereco;
import com.Ecommerce.Ecommerce.entity.Funcao;
import com.Ecommerce.Ecommerce.entity.Status;
import com.Ecommerce.Ecommerce.entity.Usuario;
import com.Ecommerce.Ecommerce.repository.EnderecoRepository;
import com.Ecommerce.Ecommerce.repository.FuncaoRepository;
import com.Ecommerce.Ecommerce.repository.StatusRepository;
import com.Ecommerce.Ecommerce.repository.UsuarioRepository;
import com.Ecommerce.Ecommerce.util.UsuarioMapper;
import com.Ecommerce.Ecommerce.util.ValidaEmail;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private FuncaoRepository funcaoRepository;

    @Autowired
    private StatusRepository statusRepository;

    public ResponseEntity<UsuarioDTO> criarUsuario(UsuarioDTO usuarioDTO, HttpServletResponse response) {
        // Validar o email
        if (!ValidaEmail.validarCaracterArroba(usuarioDTO.getEmail())){
            return ResponseEntity.status(422).build();
        }
        // Converter DTO para entidade usuario
        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);

        // Buscar e associar o endereco usando o ID
        if (usuario.getEndereco() != null && usuario.getEndereco().getId() != null) {
            Optional<Endereco> enderecoOptional = enderecoRepository.findById(usuario.getEndereco().getId());
            if (enderecoOptional.isPresent()) {
                usuario.setEndereco(enderecoOptional.get());
            } else {
                throw new RuntimeException("Endereço com ID " + usuario.getEndereco().getId() + " não encontrado.");
            }
        }
        if (usuario.getFuncao() != null && usuario.getFuncao().getId() != null) {
            Optional<Funcao> funcaoOptional = funcaoRepository.findById(usuario.getFuncao().getId());
            if (funcaoOptional.isPresent()) {
                usuario.setFuncao(funcaoOptional.get());
            } else {
                throw new RuntimeException("Funcao com ID " + usuario.getFuncao().getId() + " não encontrado.");
            }
        }
        if (usuario.getStatus() != null && usuario.getStatus().getId() != null) {
            Optional<Status> statusOptional = statusRepository.findById(usuario.getStatus().getId());
            if (statusOptional.isPresent()) {
                usuario.setStatus(statusOptional.get());
            } else {
                throw new RuntimeException("Funcao com ID " + usuario.getStatus().getId() + " não encontrado.");
            }
        }

        UsuarioDTO usuarioSalvoDTO = UsuarioMapper.toDTO(usuarioRepository.save(usuario));
        
        usuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(usuarioSalvoDTO);
    }

    public List<UsuarioDTO> obterTodosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO obterUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
    }

    public UsuarioDTO atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        usuarioExistente.setNome(usuarioDTO.getNome());
        usuarioExistente.setEmail(usuarioDTO.getEmail());
        usuarioExistente.setPassword(usuarioDTO.getPassword());

        if (usuarioDTO.getEnderecoDTO() != null && usuarioDTO.getEnderecoDTO().getId() != null) {
            Optional<Endereco> enderecoOptional = enderecoRepository.findById(usuarioDTO.getEnderecoDTO().getId());
            enderecoOptional.ifPresent(usuarioExistente::setEndereco);
        }    
        if (usuarioDTO.getFuncaoDTO() != null && usuarioDTO.getFuncaoDTO().getId() != null) {
            Optional<Funcao> funcaoOptional = funcaoRepository.findById(usuarioDTO.getFuncaoDTO().getId());
            funcaoOptional.ifPresent(usuarioExistente::setFuncao);
        }
        if (usuarioDTO.getStatusDTO() != null && usuarioDTO.getStatusDTO().getId() != null) {
            Optional<Status> statusOptional = statusRepository.findById(usuarioDTO.getStatusDTO().getId());
            statusOptional.ifPresent(usuarioExistente::setStatus);
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);
        return UsuarioMapper.toDTO(usuarioAtualizado);
    }

    public boolean deletarUsuario(Long id) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if(usuarioExistente.isPresent()){
            usuarioRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
