package com.vicentedev.api_re.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Dados para cadastro de uma nova motocicleta")
public record MotorcycleCreateRequest(
        @Schema(description = "Nome do modelo da motocicleta", example = "Super Meteor 650")
        @NotBlank(message = "Model name is required")
        @Size(max = 100, message = "Model name cannot exceed 100 characters")
        String modelName,

        @Schema(description = "Família ou categoria da moto", example = "Cruiser")
        @NotBlank(message = "Family is required")
        @Size(max = 50, message = "Family cannot exceed 50 characters")
        String family,

        @Schema(description = "Cilindrada do motor em cc", example = "648")
        @NotNull(message = "Engine CC is required")
        @Positive(message = "Engine CC must be greater than zero")
        Integer engineCc,

        @Schema(description = "Preço inicial sugerido", example = "33990.00")
        @NotNull(message = "Starting price is required")
        @Positive(message = "Starting price must be greater than zero")
        BigDecimal startingPrice,

        @Schema(description = "Descrição detalhada do modelo", example = "Cruiser premium com motor bicilíndrico paralelo de 648cc.")
        String description,

        @Schema(description = "Status de visibilidade no catálogo", example = "true", defaultValue = "true")
        Boolean active,

        @Schema(description = "Ficha técnica inicial opcional da motocicleta")
        @Valid
        TechnicalSpecRequest technicalSpec
) {
}
