package com.vicentedev.api_re.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Dados para validação do código 2FA e emissão do JWT")
public record Verify2FaRequest(
        @Schema(description = "E-mail do usuário em processo de autenticação", example = "admin@royalenfield.com.br")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Schema(description = "Código numérico de 6 dígitos recebido por e-mail", example = "123456")
        @NotBlank(message = "Code is required")
        @Pattern(regexp = "^\\d{6}$", message = "Code must be exactly 6 numeric digits")
        String code
) {
}
