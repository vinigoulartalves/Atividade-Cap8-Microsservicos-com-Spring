package com.ecodenuncia.api.dto.auth;

import com.ecodenuncia.api.model.enums.Role;

public record AuthResponseDto(
        String token,
        String tipo,
        Long usuarioId,
        String nome,
        String email,
        Role role
) {
}
