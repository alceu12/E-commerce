    package com.Ecommerce.Ecommerce.repository;

    import org.springframework.data.jpa.repository.JpaRepository;

    import com.Ecommerce.Ecommerce.entity.Produto;
    import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

    import java.util.List;

    public interface ProdutoRepository extends JpaRepository<Produto, Long>, JpaSpecificationExecutor<Produto> {
        List<Produto> findByNomeContainingIgnoreCase(String nome);

    }
