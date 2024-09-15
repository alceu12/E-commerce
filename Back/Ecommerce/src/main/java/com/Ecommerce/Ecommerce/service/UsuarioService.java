package com.Ecommerce.Ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ecommerce.Ecommerce.dto.UsuarioDTO;
import com.Ecommerce.Ecommerce.entity.Endereco;
import com.Ecommerce.Ecommerce.entity.Funcao;
import com.Ecommerce.Ecommerce.entity.Usuario;
import com.Ecommerce.Ecommerce.repository.EnderecoRepository;
import com.Ecommerce.Ecommerce.repository.FuncaoRepository;
import com.Ecommerce.Ecommerce.repository.UsuarioRepository;
import com.Ecommerce.Ecommerce.util.UsuarioMapper;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private FuncaoRepository funcaoRepository;

    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) {
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
        usuario = usuarioRepository.save(usuario);
        return UsuarioMapper.toDTO(usuario);
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
