package br.com.ecodenuncia.dto.auth;

import br.com.ecodenuncia.model.Role;

public record AuthResponse(
        Long id,
        String nome,
        String email,
        Role role,
        String token,
        String tipo
) {
    public AuthResponse(Long id, String nome, String email, Role role, String token) {
        this(id, nome, email, role, token, "Bearer");
    }
}
