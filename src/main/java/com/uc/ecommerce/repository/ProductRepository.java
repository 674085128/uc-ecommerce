package com.uc.ecommerce.repository;

import com.uc.ecommerce.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
