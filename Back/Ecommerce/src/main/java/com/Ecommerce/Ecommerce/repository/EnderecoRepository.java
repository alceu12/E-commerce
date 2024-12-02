package com.Ecommerce.Ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecommerce.Ecommerce.entity.Endereco;

import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
