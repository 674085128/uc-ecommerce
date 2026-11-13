package com.uc.ecommerce.config;

import com.uc.ecommerce.domain.Product;
import com.uc.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 启动时预置演示商品（H2 内存库每次重启清空）。
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            productRepository.save(Product.builder()
                    .name("iPhone 15")
                    .price(new BigDecimal("6999.00"))
                    .availableStock(100)
                    .frozenStock(0)
                    .build());
            productRepository.save(Product.builder()
                    .name("AirPods Pro")
                    .price(new BigDecimal("1899.00"))
                    .availableStock(50)
                    .frozenStock(0)
                    .build());
        }
    }
}
