package com.Ecommerce.Ecommerce.repository;

import com.Ecommerce.Ecommerce.entity.Cupom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CupomRepository extends JpaRepository <Cupom, Long> {
    Optional<Cupom> findByCodigoAndAtivoIsTrue(String codigo);

    Optional<Cupom> findByCodigoAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(String codigo, LocalDate hoje, LocalDate hoje1);
}
