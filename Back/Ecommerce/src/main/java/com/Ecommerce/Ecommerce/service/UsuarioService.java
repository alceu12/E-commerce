package com.Ecommerce.Ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.Ecommerce.Ecommerce.dto.EnderecoDTO;
import com.Ecommerce.Ecommerce.entity.StatusUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            usuario.setStatusUser(StatusUser.DELETED);
            usuarioRepository.save(usuario);
            return true;
        } else {
            return false;
        }
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public void updatePassword(String email, String newPassword) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
    }

    public String atualizarEndereco(Long userId, EnderecoDTO enderecoDTO) throws Exception {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(userId);
        if (!usuarioOpt.isPresent()) {
            throw new Exception("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();
        Endereco endereco = usuario.getEndereco();

        if (endereco == null) {
            // Criar novo endereço
            endereco = new Endereco();
        }

        // Atualizar os campos do endereço
        endereco.setCep(enderecoDTO.getCep());
        endereco.setRua(enderecoDTO.getRua());
        endereco.setNumero(enderecoDTO.getNumero());
        endereco.setComplemento(enderecoDTO.getComplemento());
        endereco.setBairro(enderecoDTO.getBairro());
        endereco.setCidade(enderecoDTO.getCidade());
        endereco.setEstado(enderecoDTO.getEstado());

        // Associar o endereço ao usuário
        usuario.setEndereco(endereco);
        usuario.setStatusUser(StatusUser.ACTIVE);
        // Salvar o usuário (cascade salvará o endereço)
        usuarioRepository.save(usuario);

        return "Endereço atualizado com sucesso.";
    }

    public boolean verifyIfUserExists(String login) {
        return usuarioRepository.existsByLogin(login);
    }

}
