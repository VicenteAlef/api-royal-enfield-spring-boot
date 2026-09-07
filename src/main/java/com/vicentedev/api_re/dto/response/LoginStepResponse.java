package com.vicentedev.api_re.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta da primeira etapa de login (despacho do 2FA)")
public record LoginStepResponse(
        @Schema(description = "Indica que o segundo fator de autenticação é obrigatório", example = "true")
        boolean requires2FA,

        @Schema(description = "E-mail de destino do código OTP", example = "admin@royalenfield.com.br")
        String email,

        @Schema(description = "Mensagem orientativa", example = "A 6-digit verification code has been sent to your email.")
        String message
) {
}
