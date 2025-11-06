package ru.aston.homework.intensive.userservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8081}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("User Service API")
                .version("1.0.0")
                .description("REST API for User Management with HATEOAS support")
                .contact(new Contact()
                    .name("Leonid")
                    .email("canni85@mail.ru")))
            .servers(List.of(new Server()
                    .url("http://localhost:" + serverPort)
                    .description("Development Server")
            ));
    }
}
