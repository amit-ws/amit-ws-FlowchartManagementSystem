package com.conceptile.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Flowchart management Spring Boot App")
                        .version("1.0")
                        .description("Flowchart management application using Spring Boot")
                        .termsOfService("https://www.conceptile.io/")
                        .contact(new io.swagger.v3.oas.models.info.Contact().email("support@conceptile.io"))
                        .license(new io.swagger.v3.oas.models.info.License().url("https://www.conceptile.io/"))
                );
    }
}