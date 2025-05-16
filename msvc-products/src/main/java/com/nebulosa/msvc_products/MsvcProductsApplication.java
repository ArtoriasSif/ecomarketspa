package com.nebulosa.msvc_products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

@EnableFeignClients
@SpringBootApplication
public class MsvcProductsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcProductsApplication.class, args);
	}

}
