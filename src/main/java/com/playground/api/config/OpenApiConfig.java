package com.playground.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                        .title("Spring Boot API Playground")
                        .description("This is a simple Spring Boot REST API developed for learning purposes.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("GitHub Repository")
                                .url("https://github.com/rsca7213/spring-boot-playground")
                        )

                )
                .components(new Components()
                        .addSecuritySchemes("cookieAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name("auth")
                                .description("Login is handled by a cookie automatically (httpOnly), please use the login endpoint.")
                        )
                );
    }
}
