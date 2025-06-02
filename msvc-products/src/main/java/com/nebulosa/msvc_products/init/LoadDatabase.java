package com.nebulosa.msvc_products.init;

import com.nebulosa.msvc_products.models.entities.Product;
import com.nebulosa.msvc_products.repositories.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return args -> {
            Product product1 = new Product("Pasta","4000");
            Product product2 = new Product("Cafe de Hombre","50000");

            log.info("Creating product {}", productRepository.save(product1));
            log.info("Creating product {}", productRepository.save(product2));

        };

    }
}
