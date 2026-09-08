package com.vicentedev.api_re.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para início do fluxo de autenticação")
public record LoginRequest(
        @Schema(description = "E-mail cadastrado do usuário", example = "admin@royalenfield.com.br")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Schema(description = "Senha de acesso", example = "Admin@123456")
        @NotBlank(message = "Password is required")
        String password
) {
}
