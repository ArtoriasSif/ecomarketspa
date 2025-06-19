package com.nebula.msvc_pedidos.init;

import com.nebula.msvc_pedidos.models.entitis.Pedido;
import com.nebula.msvc_pedidos.repositories.PedidoRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Random;

@Component
public class LoadDatabase implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public void run(String... args) throws Exception {
        Random random = new Random();

        Long[] sucursales = {1L, 2L, 3L}; // IDs ficticios de sucursal

        if (pedidoRepository.count() == 0) {
            for (int i = 1; i <= 100; i++) {
                Pedido pedido = new Pedido();

                pedido.setIdUsuario((long) i);
                pedido.setIdSucursal(sucursales[random.nextInt(sucursales.length)]);
                pedido.setFechaPedido(LocalDateTime.now().minusDays(random.nextInt(30)));

                pedido = pedidoRepository.save(pedido);
                log.info("Pedido creado: {}", pedido);
            }
        }
    }
}

