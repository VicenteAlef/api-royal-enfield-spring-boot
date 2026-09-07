package com.vicentedev.api_re.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Dados para atualização de uma motocicleta")
public record MotorcycleUpdateRequest(
        @Schema(description = "Nome do modelo da motocicleta", example = "Super Meteor 650 Twin")
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

        @Schema(description = "Preço inicial sugerido", example = "34990.00")
        @NotNull(message = "Starting price is required")
        @Positive(message = "Starting price must be greater than zero")
        BigDecimal startingPrice,

        @Schema(description = "Descrição detalhada do modelo", example = "Cruiser topo de linha da Royal Enfield atualizada.")
        String description,

        @Schema(description = "Status de visibilidade no catálogo", example = "true")
        Boolean active
) {
}
