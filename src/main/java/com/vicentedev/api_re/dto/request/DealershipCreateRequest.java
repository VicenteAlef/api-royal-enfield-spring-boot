package com.vicentedev.api_re.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro de uma nova concessionária")
public record DealershipCreateRequest(
        @Schema(description = "Nome da concessionária", example = "Royal Enfield Moema")
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,

        @Schema(description = "Cidade da concessionária", example = "São Paulo")
        @NotBlank(message = "City is required")
        @Size(max = 50, message = "City must not exceed 50 characters")
        String city,

        @Schema(description = "Sigla da unidade federativa (UF)", example = "SP")
        @NotBlank(message = "State is required")
        @Size(min = 2, max = 2, message = "State must be exactly 2 characters")
        @Pattern(regexp = "^[A-Za-z]{2}$", message = "State must contain 2 alphabetic characters (e.g. SP, RJ)")
        String state,

        @Schema(description = "Endereço completo", example = "Av. Ibirapuera, 2907 - Moema")
        @NotBlank(message = "Address is required")
        @Size(max = 200, message = "Address must not exceed 200 characters")
        String address,

        @Schema(description = "Telefone de contato", example = "(11) 5051-0000")
        @Size(max = 20, message = "Phone must not exceed 20 characters")
        String phone,

        @Schema(description = "E-mail de contato da loja", example = "moema@royalenfield.com.br")
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email
) {
}
