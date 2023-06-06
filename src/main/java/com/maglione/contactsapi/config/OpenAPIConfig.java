package com.maglione.contactsapi.config;


import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;


@Configuration
public class OpenAPIConfig {


    @Value("${openapi.dev-url}")
    private String devUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setEmail("maglionestefanoinfo@gmail.com");
        contact.setName("Stefano Maglione");

        Info info = new Info()
                .title("ContactsApi")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage persons and skills.");



        return new OpenAPI().info(info).servers(List.of(devServer));
    }


}
