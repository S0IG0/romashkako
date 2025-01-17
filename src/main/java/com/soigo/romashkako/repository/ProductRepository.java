package com.soigo.romashkako.repository;

import com.soigo.romashkako.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    boolean existsByIdAndDeletedIsFalse(Long id);
    @Query("SELECT p FROM Product p WHERE p.deleted = false")
    Page<Product> findAllByDeletedFalse(Specification<Product> spec, Pageable pageable);
}
