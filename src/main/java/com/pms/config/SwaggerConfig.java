package com.pms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
@Configuration
public class SwaggerConfig {


	private static final String SCHEME_NAME = "Bearer Authentication";
    private static final String SCHEME = "bearer";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes(SCHEME_NAME,
                    new SecurityScheme()
                        .type(Type.HTTP)
                        .scheme(SCHEME)
                        .bearerFormat("JWT")
                        .in(In.HEADER)
                        .name("Authorization")
                ))
            .info(new Info()
                .title("Product Management System : Spring Boot")
                .description("This project is developed by Devendra Patel")
                .version("1.0")
                .termsOfService("Terms of Service")
                .contact(new Contact()
                    .name("Admin")
                    .url("https://product.com")
                    .email("admin@gmail.com"))
                .license(new io.swagger.v3.oas.models.info.License().name("License of APIS").url("API license URL")))
            .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME));
    }
}

