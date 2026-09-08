package com.vicentedev.api_re.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro de foto na galeria da motocicleta")
public record MotorcycleGalleryRequest(
        @Schema(description = "URL da imagem (usado para links externos)", example = "https://external-cdn.com/super-meteor-lifestyle.jpg")
        @Size(max = 255, message = "Image URL cannot exceed 255 characters")
        String imageUrl,

        @Schema(description = "Legenda da imagem", example = "Painel de Instrumentos Digital")
        @Size(max = 150, message = "Caption cannot exceed 150 characters")
        String caption,

        @Schema(description = "Ordem de exibição no carrossel", example = "1", defaultValue = "0")
        Integer displayOrder
) {
}
