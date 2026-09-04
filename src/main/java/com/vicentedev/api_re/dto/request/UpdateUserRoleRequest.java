package com.vicentedev.api_re.dto.request;

import com.vicentedev.api_re.entity.Role;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleRequest(
        @NotNull(message = "Role is required")
        Role role
) {
}
