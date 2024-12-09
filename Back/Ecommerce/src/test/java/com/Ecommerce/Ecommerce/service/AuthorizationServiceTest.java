package com.Ecommerce.Ecommerce.service;

import com.Ecommerce.Ecommerce.entity.Usuario;
import com.Ecommerce.Ecommerce.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthorizationService authorizationService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("usuarioTeste");
        usuario.setPassword("senhaCriptografada");
        // Definir outros campos necessários, ex: role
    }

    @Test
    void loadUserByUsername_usuarioEncontrado() {
        // Cenário
        when(usuarioRepository.findByLogin("usuarioTeste")).thenReturn(Optional.of(usuario));

        // Execução
        var userDetails = authorizationService.loadUserByUsername("usuarioTeste");

        // Verificação
        verify(usuarioRepository, times(1)).findByLogin("usuarioTeste");
        Assertions.assertNotNull(userDetails, "UserDetails não deve ser nulo");
        Assertions.assertEquals("usuarioTeste", userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_usuarioNaoEncontrado() {
        // Cenário
        when(usuarioRepository.findByLogin("usuarioInexistente")).thenReturn(Optional.empty());

        // Execução & Verificação
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            authorizationService.loadUserByUsername("usuarioInexistente");
        });

        verify(usuarioRepository, times(1)).findByLogin("usuarioInexistente");
    }
}
