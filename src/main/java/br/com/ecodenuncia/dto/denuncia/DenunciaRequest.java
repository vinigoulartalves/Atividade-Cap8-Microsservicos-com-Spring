package br.com.ecodenuncia.dto.denuncia;

import br.com.ecodenuncia.model.CategoriaResiduo;
import br.com.ecodenuncia.model.DenunciaStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record DenunciaRequest(
        @NotBlank(message = "Titulo e obrigatorio")
        @Size(max = 120, message = "Titulo deve ter no maximo 120 caracteres")
        String titulo,

        @NotBlank(message = "Descricao e obrigatoria")
        @Size(max = 2000, message = "Descricao deve ter no maximo 2000 caracteres")
        String descricao,

        @NotBlank(message = "Endereco e obrigatorio")
        @Size(max = 200, message = "Endereco deve ter no maximo 200 caracteres")
        String endereco,

        @NotBlank(message = "Bairro e obrigatorio")
        @Size(max = 100, message = "Bairro deve ter no maximo 100 caracteres")
        String bairro,

        @NotBlank(message = "Cidade e obrigatoria")
        @Size(max = 100, message = "Cidade deve ter no maximo 100 caracteres")
        String cidade,

        @NotBlank(message = "Estado e obrigatorio")
        @Pattern(regexp = "^[A-Za-z]{2}$", message = "Estado deve conter a sigla com 2 letras")
        String estado,

        @NotNull(message = "Latitude e obrigatoria")
        @DecimalMin(value = "-90.0", message = "Latitude minima e -90")
        @DecimalMax(value = "90.0", message = "Latitude maxima e 90")
        BigDecimal latitude,

        @NotNull(message = "Longitude e obrigatoria")
        @DecimalMin(value = "-180.0", message = "Longitude minima e -180")
        @DecimalMax(value = "180.0", message = "Longitude maxima e 180")
        BigDecimal longitude,

        @NotNull(message = "Categoria e obrigatoria")
        CategoriaResiduo categoria,

        DenunciaStatus status
) {
}
