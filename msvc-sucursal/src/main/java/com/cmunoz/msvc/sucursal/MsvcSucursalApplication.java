package com.cmunoz.msvc.sucursal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcSucursalApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcSucursalApplication.class, args);
	}

}
