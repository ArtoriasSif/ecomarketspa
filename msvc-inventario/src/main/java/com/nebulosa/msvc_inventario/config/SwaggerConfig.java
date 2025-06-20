package com.nebulosa.msvc_inventario.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Contact contact = new Contact();
        contact.setName("De Lara");
        contact.setEmail("ma.delara@duocuc.cl");
        return new OpenAPI()
                .info(new Info()
                        .title("API - MSVC - Inventario")
                        .version("1.0.0")
                        .description("Este es el microservicio de Inventario, con el puedes realizar todas las consultas" +
                                "CRUD que necesitas")
                        .contact(contact)
                        .termsOfService("JAVA CLASS IN PREPARE TO DIE")
                        .summary("Esta es una API dentro de un proyecto de MSVC")
                );

    }
}
