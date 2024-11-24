package com.Ecommerce.Ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.Ecommerce.Ecommerce.entity.Usuario;

public interface UsuarioRepository extends JpaRepository <Usuario, Long> {

    UserDetails findByLogin(String username);
    
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByUsername(String username);

    List<Usuario> findByUsernameStartingWithIgnoreCase(String username);

    boolean existsByEmail(String email);
}
