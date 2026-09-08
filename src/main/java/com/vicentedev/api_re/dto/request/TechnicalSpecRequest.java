package com.vicentedev.api_re.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Dados da ficha técnica da motocicleta")
public record TechnicalSpecRequest(
        @Schema(description = "Potência máxima e rotação", example = "47 hp @ 7250 rpm")
        @Size(max = 50, message = "Power HP cannot exceed 50 characters")
        String powerHp,

        @Schema(description = "Torque máximo e rotação", example = "52.3 Nm @ 5650 rpm")
        @Size(max = 50, message = "Torque NM cannot exceed 50 characters")
        String torqueNm,

        @Schema(description = "Peso em ordem de marcha (kg)", example = "241.00")
        @Positive(message = "Weight must be greater than zero")
        BigDecimal weightKg,

        @Schema(description = "Capacidade do tanque de combustível (Litros)", example = "15.70")
        @Positive(message = "Fuel capacity must be greater than zero")
        BigDecimal fuelCapacityL,

        @Schema(description = "Altura do assento em milímetros", example = "740")
        @Positive(message = "Seat height must be greater than zero")
        Integer seatHeightMm,

        @Schema(description = "Tipo de transmissão / câmbio", example = "6 marchas com embreagem assistida e deslizante")
        @Size(max = 50, message = "Transmission cannot exceed 50 characters")
        String transmission,

        @Schema(description = "Especificação do freio dianteiro", example = "Disco único de 320mm com ABS de canal duplo")
        @Size(max = 100, message = "Front brake cannot exceed 100 characters")
        String frontBrake,

        @Schema(description = "Especificação do freio traseiro", example = "Disco único de 300mm com ABS de canal duplo")
        @Size(max = 100, message = "Rear brake cannot exceed 100 characters")
        String rearBrake,

        @Schema(description = "Sistema de arrefecimento do motor", example = "Ar e radiador de óleo")
        @Size(max = 50, message = "Cooling system cannot exceed 50 characters")
        String coolingSystem
) {
}
