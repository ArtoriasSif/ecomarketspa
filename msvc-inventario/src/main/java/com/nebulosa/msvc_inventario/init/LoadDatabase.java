package com.nebulosa.msvc_inventario.init;

import com.nebulosa.msvc_inventario.models.entities.Inventory;
import com.nebulosa.msvc_inventario.repositories.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class LoadDatabase implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (inventoryRepository.count() == 0) {
            Random random = new Random();
            int maxProductoId = 50;
            Long[] sucursales = {1L, 2L, 3L};

            for (long productoId = 1; productoId <= maxProductoId; productoId++) {
                for (Long sucursalId : sucursales) {
                    long cantidad = random.nextInt(301); // 0 a 300 unidades

                    Inventory inventario = new Inventory(null, productoId, sucursalId, cantidad);
                    inventoryRepository.save(inventario);
                    log.info("Creado inventario: ProductoId={} SucursalId={} Cantidad={}", productoId, sucursalId, cantidad);
                }
            }
        }
    }
}