package br.com.ecodenuncia.dto.usuario;

import br.com.ecodenuncia.model.Role;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        Role role
) {
}
