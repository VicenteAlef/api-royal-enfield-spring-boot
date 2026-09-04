package com.vicentedev.api_re.dto.response;

public record LoginStepResponse(
        boolean requires2FA,
        String email,
        String message
) {
}
