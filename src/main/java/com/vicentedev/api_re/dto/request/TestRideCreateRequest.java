package com.vicentedev.api_re.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Dados para solicitação de agendamento de test ride")
public record TestRideCreateRequest(
        @Schema(description = "Nome completo do interessado", example = "Carlos Silva")
        @NotBlank(message = "Customer name is required")
        @Size(max = 100, message = "Customer name must not exceed 100 characters")
        String customerName,

        @Schema(description = "E-mail de contato do cliente", example = "carlos.silva@email.com")
        @NotBlank(message = "Customer email is required")
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Customer email must not exceed 100 characters")
        String customerEmail,

        @Schema(description = "Telefone ou WhatsApp do cliente", example = "(11) 98765-4321")
        @NotBlank(message = "Customer phone is required")
        @Size(max = 20, message = "Customer phone must not exceed 20 characters")
        String customerPhone,

        @Schema(description = "Data e hora desejadas para o test ride (formato ISO-8601)", example = "2026-09-15T14:30:00Z")
        @NotNull(message = "Preferred date is required")
        @Future(message = "Preferred date must be a future date and time")
        OffsetDateTime preferredDate,

        @Schema(description = "UUID da motocicleta desejada", example = "ad7f150a-516c-49d2-b72c-53d077045994")
        @NotNull(message = "Motorcycle ID is required")
        UUID motorcycleId,

        @Schema(description = "UUID da variante/cor específica desejada (opcional)", example = "11111111-2222-3333-4444-555555555555")
        UUID variantId,

        @Schema(description = "UUID da concessionária onde será realizado o test ride", example = "11111111-2222-3333-4444-555555555555")
        @NotNull(message = "Dealership ID is required")
        UUID dealershipId
) {
}
