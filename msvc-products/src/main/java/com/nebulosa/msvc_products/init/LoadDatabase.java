package com.nebulosa.msvc_products.init;

import com.nebulosa.msvc_products.models.entities.Product;
import com.nebulosa.msvc_products.repositories.ProductRepository;

import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LoadDatabase implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Autowired
    ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(new Locale("pt", "BR"));

        if (productRepository.count() == 0) {
            for (int i = 0; i < 50; i++) {
                Product producto = new Product();

                producto.setNombreProducto(faker.commerce().productName()); // Ej: "Jabón natural"
                producto.setPrecio(faker.number().randomDouble(0, 1000, 10000)); // Precio entre 1000 y 10000

                producto = productRepository.save(producto);
                log.info("Producto creado: {}", producto);
            }
            log.info("Se crearon 50 productos de prueba");
        } else {
            log.info("Ya existen productos, no se insertó ninguno nuevo");
        }
    }
}

