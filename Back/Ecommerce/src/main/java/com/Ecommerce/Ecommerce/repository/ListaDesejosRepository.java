package com.Ecommerce.Ecommerce.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecommerce.Ecommerce.entity.ListaDesejos;

public interface ListaDesejosRepository extends JpaRepository <ListaDesejos, Long> {
    List<ListaDesejos> findByUsuarioId(Long usuarioId);
}
