package br.com.ecodenuncia.api.dto;

import br.com.ecodenuncia.api.model.CategoriaResiduo;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DenunciaRequest(

        @NotBlank(message = "O titulo e obrigatorio")
        @Size(min = 5, max = 150, message = "O titulo deve ter entre 5 e 150 caracteres")
        String titulo,

        @NotBlank(message = "A descricao e obrigatoria")
        @Size(min = 10, max = 2000, message = "A descricao deve ter entre 10 e 2000 caracteres")
        String descricao,

        @NotBlank(message = "O endereco e obrigatorio")
        @Size(max = 200)
        String endereco,

        @NotBlank(message = "O bairro e obrigatorio")
        @Size(max = 100)
        String bairro,

        @NotBlank(message = "A cidade e obrigatoria")
        @Size(max = 100)
        String cidade,

        @NotBlank(message = "O estado e obrigatorio")
        @Pattern(regexp = "^[A-Z]{2}$", message = "Estado deve ter 2 letras maiusculas (ex: SP)")
        String estado,

        @NotNull(message = "A latitude e obrigatoria")
        @DecimalMin(value = "-90.0", message = "Latitude minima e -90")
        @DecimalMax(value = "90.0", message = "Latitude maxima e 90")
        BigDecimal latitude,

        @NotNull(message = "A longitude e obrigatoria")
        @DecimalMin(value = "-180.0", message = "Longitude minima e -180")
        @DecimalMax(value = "180.0", message = "Longitude maxima e 180")
        BigDecimal longitude,

        @NotNull(message = "A categoria do residuo e obrigatoria")
        CategoriaResiduo categoria
) {
}
