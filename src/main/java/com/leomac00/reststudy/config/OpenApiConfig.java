package com.leomac00.reststudy.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("rest-with-spring-boot-and-java")
                        .version("v1")
                        .description("rest study project")
                        .license(
                                new License()
                                        .name("Apache 2.0")
                        ));
    }
}
