package com.Ecommerce.Ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.Ecommerce.Ecommerce.entity.StatusUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Ecommerce.Ecommerce.dto.UsuarioDTO;
import com.Ecommerce.Ecommerce.entity.Endereco;
import com.Ecommerce.Ecommerce.entity.Funcao;
import com.Ecommerce.Ecommerce.entity.Usuario;
import com.Ecommerce.Ecommerce.repository.EnderecoRepository;
import com.Ecommerce.Ecommerce.repository.FuncaoRepository;
import com.Ecommerce.Ecommerce.repository.UsuarioRepository;
import com.Ecommerce.Ecommerce.util.UsuarioMapper;
import com.Ecommerce.Ecommerce.util.ValidaEmail;


@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private FuncaoRepository funcaoRepository;


    public ResponseEntity<UsuarioDTO> criarUsuario(UsuarioDTO usuarioDTO) {
        // Validar o email
        if (!ValidaEmail.validarCaracterArroba(usuarioDTO.getEmail())) {
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

        UsuarioDTO usuarioSalvoDTO = UsuarioMapper.toDTO(usuarioRepository.save(usuario));
        usuario.setStatusUser(StatusUser.ACTIVE);
        usuarioRepository.save(usuario);
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
        usuarioExistente.setStatusUser(StatusUser.valueOf(usuarioDTO.getStatusUser()));
        if (usuarioDTO.getEnderecoDTO() != null && usuarioDTO.getEnderecoDTO().getId() != null) {
            Optional<Endereco> enderecoOptional = enderecoRepository.findById(usuarioDTO.getEnderecoDTO().getId());
            enderecoOptional.ifPresent(usuarioExistente::setEndereco);
        }
        if (usuarioDTO.getFuncaoDTO() != null && usuarioDTO.getFuncaoDTO().getId() != null) {
            Optional<Funcao> funcaoOptional = funcaoRepository.findById(usuarioDTO.getFuncaoDTO().getId());
            funcaoOptional.ifPresent(usuarioExistente::setFuncao);
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);
        return UsuarioMapper.toDTO(usuarioAtualizado);
    }

    public boolean deletarUsuario(Long id) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if(usuarioExistente.isPresent()){
            Usuario usuario = usuarioExistente.get();
            usuario.setStatusUser(StatusUser.DELETED);
            usuarioRepository.save(usuario);
            return true;
        } else {
            return false;
        }
    }
}
