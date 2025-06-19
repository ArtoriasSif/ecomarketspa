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

            // Precios reales según tus productos
            Map<Long, Double> precios = Map.of(
                    1L, 2500.0,
                    2L, 4000.0,
                    3L, 6640.0
            );

            // Supón que ya existen pedidos con ID del 1 al 10
            for (long idPedido = 1; idPedido <= 10; idPedido++) {
                List<Long> productos = new ArrayList<>(precios.keySet());
                Collections.shuffle(productos); // para variar el orden

                int cantidadItems = random.nextInt(3) + 1; // 1 a 3 productos por pedido

                for (int i = 0; i < cantidadItems; i++) {
                    Long idProducto = productos.get(i);
                    Long cantidad = (long) (random.nextInt(3) + 1); // 1 a 3 unidades
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
