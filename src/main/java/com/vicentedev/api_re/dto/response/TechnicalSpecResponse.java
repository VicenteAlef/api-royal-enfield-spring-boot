package com.vicentedev.api_re.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Ficha técnica completa da motocicleta")
public record TechnicalSpecResponse(
        @Schema(description = "Identificador único da ficha técnica", example = "e4a2d890-1c23-4b56-7890-abcdef123456")
        UUID id,

        @Schema(description = "Potência máxima do motor", example = "47 hp @ 7250 rpm")
        String powerHp,

        @Schema(description = "Torque máximo do motor", example = "52.3 Nm @ 5650 rpm")
        String torqueNm,

        @Schema(description = "Peso em ordem de marcha (kg)", example = "241.00")
        BigDecimal weightKg,

        @Schema(description = "Capacidade do tanque (Litros)", example = "15.70")
        BigDecimal fuelCapacityL,

        @Schema(description = "Altura do assento em milímetros", example = "740")
        Integer seatHeightMm,

        @Schema(description = "Transmissão", example = "6 marchas com embreagem assistida e deslizante")
        String transmission,

        @Schema(description = "Freio dianteiro", example = "Disco único de 320mm com ABS de canal duplo")
        String frontBrake,

        @Schema(description = "Freio traseiro", example = "Disco único de 300mm com ABS de canal duplo")
        String rearBrake,

        @Schema(description = "Sistema de refrigeração", example = "Ar e radiador de óleo")
        String coolingSystem,

        @Schema(description = "Data de cadastro", example = "2026-08-28T19:54:16Z")
        OffsetDateTime createdAt,

        @Schema(description = "Data da última atualização", example = "2026-08-28T19:54:16Z")
        OffsetDateTime updatedAt
) {
}
