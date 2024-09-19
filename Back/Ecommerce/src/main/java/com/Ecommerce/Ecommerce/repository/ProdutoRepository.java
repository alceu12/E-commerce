package com.Ecommerce.Ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecommerce.Ecommerce.entity.Produto;

public interface ProdutoRepository extends JpaRepository <Produto, Long> {

}
