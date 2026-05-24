package br.com.ecodenuncia.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para login (POST /auth/login).
 * Bean Validation: @NotBlank, @Email, @Size.
 */
public record LoginRequest(

        @NotBlank(message = "O email e obrigatorio")
        @Email(message = "Email invalido")
        @Size(max = 150, message = "O email deve ter no maximo 150 caracteres")
        String email,

        @NotBlank(message = "A senha e obrigatoria")
        @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
        String senha
) {
}
