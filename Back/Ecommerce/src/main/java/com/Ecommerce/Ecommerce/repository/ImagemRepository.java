package com.Ecommerce.Ecommerce.repository;

import com.Ecommerce.Ecommerce.entity.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagemRepository extends JpaRepository<Imagem, Long> {
}
