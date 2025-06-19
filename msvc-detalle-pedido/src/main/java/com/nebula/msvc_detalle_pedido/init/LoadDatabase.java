package com.nebula.msvc_detalle_pedido.init;

import com.nebula.msvc_detalle_pedido.models.entities.DetallePedido;
import com.nebula.msvc_detalle_pedido.repositories.DetallePedidoRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LoadDatabase implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Override
    public void run(String... args) throws Exception {
        if (detallePedidoRepository.count() == 0) {
            Random random = new Random();

            // Generar precios para productos del 1 al 50
            Map<Long, Double> precios = new HashMap<>();
            for (long idProducto = 1; idProducto <= 50; idProducto++) {
                // Precio random entre 1000 y 10000
                precios.put(idProducto, 1000.0 + random.nextInt(9001));
            }

            for (long idPedido = 1; idPedido <= 50; idPedido++) {
                List<Long> productos = new ArrayList<>(precios.keySet());
                Collections.shuffle(productos);

                int cantidadItems = random.nextInt(5) + 1; // 1 a 5 productos por pedido

                for (int i = 0; i < cantidadItems; i++) {
                    Long idProducto = productos.get(i);
                    Long cantidad = (long) (random.nextInt(5) + 1); // 1 a 5 unidades
                    Double precio = precios.get(idProducto);
                    Double subTotal = cantidad * precio;

                    DetallePedido detalle = new DetallePedido();
                    detalle.setIdPedido(idPedido);
                    detalle.setIdProducto(idProducto);
                    detalle.setCantidad(cantidad);
                    detalle.setSubTotal(subTotal);

                    detallePedidoRepository.save(detalle);
                    log.info("Detalle generado: {}", detalle);
                }
            }
        }
    }
}