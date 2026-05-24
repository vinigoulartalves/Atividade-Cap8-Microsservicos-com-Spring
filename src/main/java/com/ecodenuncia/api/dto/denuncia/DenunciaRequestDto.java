package com.ecodenuncia.api.dto.denuncia;

import com.ecodenuncia.api.model.enums.CategoriaResiduo;
import com.ecodenuncia.api.model.enums.StatusDenuncia;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record DenunciaRequestDto(
        @NotBlank(message = "Título é obrigatório")
        @Size(max = 150, message = "Título deve ter no máximo 150 caracteres")
        String titulo,

        @NotBlank(message = "Descrição é obrigatória")
        @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
        String descricao,

        @NotBlank(message = "Endereço é obrigatório")
        @Size(max = 255, message = "Endereço deve ter no máximo 255 caracteres")
        String endereco,

        @NotBlank(message = "Bairro é obrigatório")
        @Size(max = 120, message = "Bairro deve ter no máximo 120 caracteres")
        String bairro,

        @NotBlank(message = "Cidade é obrigatória")
        @Size(max = 120, message = "Cidade deve ter no máximo 120 caracteres")
        String cidade,

        @NotBlank(message = "Estado é obrigatório")
        @Size(min = 2, max = 2, message = "Estado deve conter 2 caracteres")
        String estado,

        @NotNull(message = "Latitude é obrigatória")
        @DecimalMin(value = "-90.0", message = "Latitude mínima é -90")
        @DecimalMax(value = "90.0", message = "Latitude máxima é 90")
        BigDecimal latitude,

        @NotNull(message = "Longitude é obrigatória")
        @DecimalMin(value = "-180.0", message = "Longitude mínima é -180")
        @DecimalMax(value = "180.0", message = "Longitude máxima é 180")
        BigDecimal longitude,

        @NotNull(message = "Categoria do resíduo é obrigatória")
        CategoriaResiduo categoriaResiduo,

        @NotNull(message = "Status da denúncia é obrigatório")
        StatusDenuncia status
) {
}
