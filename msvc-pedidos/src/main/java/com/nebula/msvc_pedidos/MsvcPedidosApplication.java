package com.nebula.msvc_pedidos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcPedidosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcPedidosApplication.class, args);
	}

}
