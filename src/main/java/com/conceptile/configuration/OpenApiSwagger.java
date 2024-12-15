package com.conceptile.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(info = @Info(
        title = "Flowchart management Spring Boot App"
        , version = "1.0"
        , description = "Flowchart management application using Spring boot"
        , termsOfService = "https://www.conceptile.io/"
        , contact = @Contact(email = "https://www.conceptile.io/")
        , license = @License(url = "https://www.conceptile.io/")
)
)
public class OpenApiSwagger {
}