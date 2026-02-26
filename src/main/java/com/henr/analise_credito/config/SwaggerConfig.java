package com.henr.analise_credito.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Análise de Crédito API")
                .version("1.0.0")
                .description("API para análise e concessão de crédito")
                .contact(new Contact()
                    .name("Luiz Henrique")
                    .email("lhcontato2020@gmail.com")
                )
            );
    }
}