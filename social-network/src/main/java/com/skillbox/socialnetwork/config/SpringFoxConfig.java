package com.skillbox.socialnetwork.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;


// Сваггер будет доступен на  http://localhost:8086/swagger-ui.html
// JSON при этом находится на http://localhost:8086/v3/api-docs
// позже выгружу это же в yaml в корень
@Configuration
@OpenAPIDefinition(info = @Info(title = "Zerone Social Network API", version = "v1"))
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "jwt", paramName = "Authorization", in = SecuritySchemeIn.HEADER, scheme = "bearer" )
public class SpringFoxConfig {
}