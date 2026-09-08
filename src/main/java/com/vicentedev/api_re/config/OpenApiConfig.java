package com.vicentedev.api_re.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI royalEnfieldOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Royal Enfield REST API")
                        .description("API REST para o ecossistema da Royal Enfield, gerenciando catálogo de motocicletas, variantes e cores, fichas técnicas, galeria de imagens, rede de concessionárias, agendamentos de test-ride e controle de usuários com autenticação em dois fatores (2FA) e JWT.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Vicente Alef - GitHub")
                                .url("https://github.com/VicenteAlef"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("vicentedeveloper.com - Website")
                        .url("https://vicentedeveloper.com"))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Insira o token JWT gerado após a autenticação (sem a palavra 'Bearer ', apenas o token).")));
    }
}
