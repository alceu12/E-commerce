package com.Ecommerce.Ecommerce.repository;

import com.Ecommerce.Ecommerce.entity.Cupom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CupomRepository extends JpaRepository <Cupom, Long> {
    Optional<Cupom> findByCodigoAndAtivoIsTrue(String codigo);
}
