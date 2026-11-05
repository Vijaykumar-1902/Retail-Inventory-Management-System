package com.retail.inventory.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.retail.inventory.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByPriceBetween(
            BigDecimal minPrice,
            BigDecimal maxPrice
    );
    List<Product> findByCategoryId(Long categoryId);
}