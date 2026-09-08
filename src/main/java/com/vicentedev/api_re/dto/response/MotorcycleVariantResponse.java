package com.vicentedev.api_re.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Dados detalhados de uma variante/cor da motocicleta")
public record MotorcycleVariantResponse(
        @Schema(description = "Identificador único da variante", example = "11111111-2222-3333-4444-555555555555")
        UUID id,

        @Schema(description = "UUID da motocicleta pai", example = "ad7f150a-516c-49d2-b72c-53d077045994")
        UUID motorcycleId,

        @Schema(description = "Nome da versão/variante", example = "Astral")
        String variantName,

        @Schema(description = "Nome da cor", example = "Astral Black")
        String colorName,

        @Schema(description = "Código hexadecimal da cor", example = "#0A0A0A")
        String hexColorCode,

        @Schema(description = "Preço da variante", example = "33990.00")
        BigDecimal price,

        @Schema(description = "URL da imagem da variante", example = "/uploads/variants/astral-black.webp")
        String imageUrl,

        @Schema(description = "Acessórios e acabamentos exclusivos inclusos", example = "Espelhos clássicos, rodas de liga leve")
        String includedAccessories,

        @Schema(description = "Status de disponibilidade da variante", example = "true")
        Boolean active,

        @Schema(description = "Data de cadastro", example = "2026-08-28T19:54:16Z")
        OffsetDateTime createdAt,

        @Schema(description = "Data da última atualização", example = "2026-08-28T19:54:16Z")
        OffsetDateTime updatedAt
) {
}
