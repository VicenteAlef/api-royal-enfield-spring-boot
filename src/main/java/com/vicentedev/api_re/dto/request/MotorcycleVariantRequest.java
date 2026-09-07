package com.vicentedev.api_re.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Dados para cadastro ou atualização de variante/cor da motocicleta")
public record MotorcycleVariantRequest(
        @Schema(description = "Nome da variante", example = "Interstellar")
        @NotBlank(message = "Variant name is required")
        @Size(max = 100, message = "Variant name cannot exceed 100 characters")
        String variantName,

        @Schema(description = "Nome da cor", example = "Interstellar Grey")
        @NotBlank(message = "Color name is required")
        @Size(max = 100, message = "Color name cannot exceed 100 characters")
        String colorName,

        @Schema(description = "Código hexadecimal da cor", example = "#6E7072")
        @Size(max = 10, message = "Hex color code cannot exceed 10 characters")
        String hexColorCode,

        @Schema(description = "Preço da variante", example = "34990.00")
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        BigDecimal price,

        @Schema(description = "URL da imagem da variante (caso já hospedada)", example = "/uploads/variants/interstellar-grey.webp")
        @Size(max = 255, message = "Image URL cannot exceed 255 characters")
        String imageUrl,

        @Schema(description = "Acessórios e acabamentos exclusivos inclusos", example = "Pintura em dois tons, detalhes pretos")
        String includedAccessories,

        @Schema(description = "Status de disponibilidade da variante", example = "true")
        Boolean active
) {
}
