package com.vicentedev.api_re.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Dados de uma imagem na galeria da motocicleta")
public record MotorcycleGalleryResponse(
        @Schema(description = "Identificador único do item da galeria", example = "99999999-8888-7777-6666-555555555555")
        UUID id,

        @Schema(description = "UUID da motocicleta associada", example = "ad7f150a-516c-49d2-b72c-53d077045994")
        UUID motorcycleId,

        @Schema(description = "URL da imagem", example = "/uploads/gallery/front-view.jpg")
        String imageUrl,

        @Schema(description = "Legenda descritiva da imagem", example = "Vista Frontal")
        String caption,

        @Schema(description = "Ordem de exibição no carrossel", example = "1")
        Integer displayOrder,

        @Schema(description = "Data de envio da imagem", example = "2026-08-28T19:54:16Z")
        OffsetDateTime createdAt
) {
}
