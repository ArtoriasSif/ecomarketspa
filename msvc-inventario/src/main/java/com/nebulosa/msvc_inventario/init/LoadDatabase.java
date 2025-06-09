package com.nebulosa.msvc_inventario.init;

import com.nebulosa.msvc_inventario.models.entities.Inventory;
import com.nebulosa.msvc_inventario.repositories.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initInventory(InventoryRepository inventoryRepository) {
        return args -> {
            if (inventoryRepository.count() == 0) {
                Inventory inv1 = new Inventory(null, 1L, 1L, 100L); // producto 1 en sucursal 1
                Inventory inv2 = new Inventory(null, 2L, 1L, 200L); // producto 2 en sucursal 1
                Inventory inv3 = new Inventory(null, 3L, 1L, 50L);  // producto 1 en sucursal 2
                Inventory inv4 = new Inventory(null, 1L, 2L, 50L);  // producto 1 en sucursal 2

                log.info("Creating inventory {}",inventoryRepository.save(inv1));
                log.info("Creating inventory {}",inventoryRepository.save(inv2));
                log.info("Creating inventory {}",inventoryRepository.save(inv3));
                log.info("Creating inventory {}",inventoryRepository.save(inv4));
            }
        };
    }
}
