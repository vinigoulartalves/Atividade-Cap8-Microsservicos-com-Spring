package br.com.ecodenuncia.api.dto;

import br.com.ecodenuncia.api.model.Role;

public record AuthResponse(
        String token,
        String tipo,
        Long usuarioId,
        String nome,
        String email,
        Role role
) {
    public static AuthResponse bearer(String token, Long usuarioId, String nome, String email, Role role) {
        return new AuthResponse(token, "Bearer", usuarioId, nome, email, role);
    }
}
