package com.ecommerce.ecommerce_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ecommerceOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Development Server");

        Contact contact = new Contact();
        contact.setName("Tu Nombre");
        contact.setEmail("tu@email.com");
        contact.setUrl("https://github.com/tu-usuario");

        Info info = new Info()
                .title("E-commerce API")
                .version("1.0.0")
                .description("RESTful API for e-commerce platform with categories, products, and orders management. " +
                        "Features include inventory management, order processing, and comprehensive filtering.")
                .contact(contact);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}