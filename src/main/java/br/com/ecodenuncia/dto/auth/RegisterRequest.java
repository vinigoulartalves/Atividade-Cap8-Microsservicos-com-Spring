package br.com.ecodenuncia.dto.auth;

import br.com.ecodenuncia.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Nome e obrigatorio")
        @Size(max = 120, message = "Nome deve ter no maximo 120 caracteres")
        String nome,

        @NotBlank(message = "Email e obrigatorio")
        @Email(message = "Email deve ser valido")
        @Size(max = 160, message = "Email deve ter no maximo 160 caracteres")
        String email,

        @NotBlank(message = "Senha e obrigatoria")
        @Size(min = 6, max = 80, message = "Senha deve ter entre 6 e 80 caracteres")
        String senha,

        Role role
) {
}
