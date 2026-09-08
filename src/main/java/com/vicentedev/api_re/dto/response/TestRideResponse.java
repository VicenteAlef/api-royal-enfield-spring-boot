package com.vicentedev.api_re.dto.response;

import com.vicentedev.api_re.entity.TestRideStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "Dados detalhados do agendamento de test ride")
public record TestRideResponse(
        @Schema(description = "Identificador único do agendamento", example = "99999999-aaaa-bbbb-cccc-dddddddddddd")
        UUID id,

        @Schema(description = "Nome do cliente", example = "Carlos Silva")
        String customerName,

        @Schema(description = "E-mail do cliente", example = "carlos.silva@email.com")
        String customerEmail,

        @Schema(description = "Telefone do cliente", example = "(11) 98765-4321")
        String customerPhone,

        @Schema(description = "Data e hora agendadas", example = "2026-09-15T14:30:00Z")
        OffsetDateTime preferredDate,

        @Schema(description = "Status do agendamento", example = "PENDING")
        TestRideStatus status,

        @Schema(description = "Resumo da motocicleta selecionada")
        MotorcycleSummary motorcycle,

        @Schema(description = "Resumo da variante/cor selecionada (se houver)")
        VariantSummary variant,

        @Schema(description = "Dados da concessionária do agendamento")
        DealershipResponse dealership,

        @Schema(description = "Data de solicitação", example = "2026-08-31T19:30:00Z")
        OffsetDateTime createdAt,

        @Schema(description = "Data da última atualização", example = "2026-08-31T19:30:00Z")
        OffsetDateTime updatedAt
) {
    @Schema(description = "Resumo da motocicleta no agendamento de test ride")
    public record MotorcycleSummary(
            @Schema(description = "UUID da moto", example = "ad7f150a-516c-49d2-b72c-53d077045994")
            UUID id,

            @Schema(description = "Modelo", example = "Super Meteor 650")
            String modelName,

            @Schema(description = "Família", example = "Cruiser")
            String family,

            @Schema(description = "Cilindrada", example = "648")
            Integer engineCc
    ) {
    }

    @Schema(description = "Resumo da variante no agendamento de test ride")
    public record VariantSummary(
            @Schema(description = "UUID da variante", example = "11111111-2222-3333-4444-555555555555")
            UUID id,

            @Schema(description = "Nome da variante", example = "Astral")
            String variantName,

            @Schema(description = "Nome da cor", example = "Astral Black")
            String colorName,

            @Schema(description = "URL da foto da variante", example = "/uploads/variants/astral-black.webp")
            String imageUrl
    ) {
    }
}
