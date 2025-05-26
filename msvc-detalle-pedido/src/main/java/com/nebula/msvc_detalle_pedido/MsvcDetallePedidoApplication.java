package com.nebula.msvc_detalle_pedido;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcDetallePedidoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcDetallePedidoApplication.class, args);
	}

}
