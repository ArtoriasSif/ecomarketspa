package com.nebula.msvc_pedidos.init;

import com.nebula.msvc_pedidos.models.entitis.Pedido;
import com.nebula.msvc_pedidos.repositories.PedidoRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Random;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initPedidos(PedidoRepository pedidoRepository) {
        return args -> {
            if (pedidoRepository.count() == 0) {
                Faker faker = new Faker(new Locale("es", "CL"));
                Random random = new Random();


                Long[] sucursales = {1L, 2L, 3L};


                for (int i = 1; i <= 10; i++) {
                    Pedido pedido = new Pedido();
                    pedido.setIdUsuario((long) i);
                    pedido.setIdSucursal(sucursales[random.nextInt(sucursales.length)]); // sucursal aleatoria
                    pedido.setFechaPedido(LocalDateTime.now().minusDays(random.nextInt(30))); // fecha radon sysdate between 30 dias

                    log.info("Pedido creado: {}",pedidoRepository.save(pedido));
                }
            }
        };
    }
}

