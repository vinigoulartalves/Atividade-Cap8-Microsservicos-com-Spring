package br.com.ecodenuncia.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "O nome e obrigatorio")
        @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres")
        String nome,

        @NotBlank(message = "O email e obrigatorio")
        @Email(message = "Email invalido")
        @Size(max = 150)
        String email,

        @NotBlank(message = "A senha e obrigatoria")
        @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
        String senha
) {
}
