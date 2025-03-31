package com.example.shopapp_backend.repository;

import com.example.shopapp_backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Boolean existsByName(String name);
    Page<Product> findAll(Pageable pageable);
    @Query("select p from Product p where (:categoryId is null or :categoryId = 0 or p.category.id = :categoryId) and (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> searchProducts(@Param("categoryId") Long categoryId, @Param("keyword") String keyword, Pageable pageable);

}
