package com.vicentedev.api_re.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Dados detalhados de uma concessionária Royal Enfield")
public record DealershipResponse(
        @Schema(description = "Identificador único da concessionária", example = "11111111-2222-3333-4444-555555555555")
        UUID id,

        @Schema(description = "Nome da loja", example = "Royal Enfield Moema")
        String name,

        @Schema(description = "Cidade", example = "São Paulo")
        String city,

        @Schema(description = "Estado / UF", example = "SP")
        String state,

        @Schema(description = "Endereço completo", example = "Av. Ibirapuera, 2907 - Moema")
        String address,

        @Schema(description = "Telefone de contato", example = "(11) 5051-0000")
        String phone,

        @Schema(description = "E-mail de contato", example = "moema@royalenfield.com.br")
        String email,

        @Schema(description = "Data de cadastro", example = "2026-08-31T19:30:00Z")
        OffsetDateTime createdAt,

        @Schema(description = "Data da última atualização", example = "2026-08-31T19:30:00Z")
        OffsetDateTime updatedAt
) {
}
