package com.Ecommerce.Ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecommerce.Ecommerce.entity.Usuario;

public interface UsuarioRepository extends JpaRepository <Usuario, Long> {

}
