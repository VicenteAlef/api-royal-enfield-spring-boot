package com.vicentedev.api_re.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Resumo dos dados da motocicleta para listagem")
public record MotorcycleSummaryResponse(
        @Schema(description = "Identificador único da motocicleta", example = "ad7f150a-516c-49d2-b72c-53d077045994")
        UUID id,

        @Schema(description = "Nome do modelo", example = "Super Meteor 650")
        String modelName,

        @Schema(description = "Família da moto", example = "Cruiser")
        String family,

        @Schema(description = "Cilindrada em cc", example = "648")
        Integer engineCc,

        @Schema(description = "Preço inicial a partir de", example = "33990.00")
        BigDecimal startingPrice,

        @Schema(description = "Descrição da motocicleta", example = "Cruiser premium com motor bicilíndrico paralelo de 648cc.")
        String description,

        @Schema(description = "Status de visibilidade no catálogo", example = "true")
        Boolean active,

        @Schema(description = "URL da imagem principal/destaque", example = "/uploads/variants/celestial-red.webp")
        String mainImageUrl,

        @Schema(description = "Total de variantes e cores cadastradas", example = "3")
        Integer variantCount,

        @Schema(description = "Data de cadastro", example = "2026-08-28T19:54:16Z")
        OffsetDateTime createdAt,

        @Schema(description = "Data da última atualização", example = "2026-08-28T19:54:16Z")
        OffsetDateTime updatedAt
) {
}
