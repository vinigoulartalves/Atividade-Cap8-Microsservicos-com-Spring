package com.ecodenuncia.dto.auth;

import com.ecodenuncia.model.Role;

public record AuthResponse(
        String token,
        String tipo,
        Long usuarioId,
        String nome,
        String email,
        Role role
) {
    public static AuthResponse of(String token, Long usuarioId, String nome, String email, Role role) {
        return new AuthResponse(token, "Bearer", usuarioId, nome, email, role);
    }
}
