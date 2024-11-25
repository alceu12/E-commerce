package com.Ecommerce.Ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.Ecommerce.Ecommerce.entity.Usuario;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByLogin(String login);

    // Método para buscar por email
    Optional<Usuario> findByEmail(String email);

    // Método para verificar existência por email
    boolean existsByEmail(String email);

    boolean existsByLogin(String login);
}
