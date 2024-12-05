package com.Ecommerce.Ecommerce.repository;

import com.Ecommerce.Ecommerce.entity.StatusPedido;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecommerce.Ecommerce.entity.Pedido;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findAllByUsuarioIdOrderByIdDesc(Long id);
}
