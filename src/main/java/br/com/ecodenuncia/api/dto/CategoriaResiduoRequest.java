package br.com.ecodenuncia.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para cadastro (e atualizacao) de Categoria de Residuo.
 * Usa Bean Validation para garantir os campos obrigatorios e os tamanhos.
 */
public record CategoriaResiduoRequest(

        @NotBlank(message = "O nome da categoria e obrigatorio")
        @Size(min = 2, max = 80, message = "O nome deve ter entre 2 e 80 caracteres")
        String nome,

        @NotBlank(message = "A descricao da categoria e obrigatoria")
        @Size(min = 5, max = 255, message = "A descricao deve ter entre 5 e 255 caracteres")
        String descricao
) {
}
