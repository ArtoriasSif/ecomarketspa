package han.msvc_feedback.mscv_feedback.config;

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
                contact.setName("Carlos Muñoz");
                contact.setEmail("carl.munozl@duocuc.cl");
        return new OpenAPI()
                .info(new Info()
                        .title("API - MSVC - Feedback")
                        .version("1.0.0")
                        .description("API de MSVC - Feedback, con el puedes realziar todas las consultas" +
                                "CRUD que necesites")
                        .contact(contact)
                        .termsOfService("NO COLAPSAR LA API")
                        .summary("Esta es una API de MSVC - Feedback")


                );
    }
}
