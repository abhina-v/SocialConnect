package com.interestconnect.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI interestConnectOpenAPI() {

        return new OpenAPI()

                .info(new Info()
                        .title("Interest Connect API")
                        .description(
                                "Interest Connect is a real-time networking platform where users can create topics, connect with others, and chat securely using JWT authentication and WebSockets."
                        )
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Abhinav Reddy")
                                .email("potlaabhinavreddy@example.com")))

                .externalDocs(
                        new ExternalDocumentation()
                                .description("Interest Connect Backend Documentation"))

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("Bearer Authentication"))

                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "Bearer Authentication",
                                        new SecurityScheme()
                                                .name("Authorization")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}