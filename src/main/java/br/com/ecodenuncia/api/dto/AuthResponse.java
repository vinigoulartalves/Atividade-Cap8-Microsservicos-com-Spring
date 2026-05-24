package br.com.ecodenuncia.api.dto;

import br.com.ecodenuncia.api.model.Role;

/**
 * DTO de saida para resposta de autenticacao (login/registro).
 * Contem o JWT e os dados publicos do usuario autenticado.
 */
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
