package com.Ecommerce.Ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecommerce.Ecommerce.entity.Pedido;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findAllByUsuarioIdOrderByDataPedidoDesc(Long id);
}
