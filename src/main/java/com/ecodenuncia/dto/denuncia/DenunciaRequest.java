package com.ecodenuncia.dto.denuncia;

import com.ecodenuncia.model.CategoriaResiduo;
import com.ecodenuncia.model.StatusDenuncia;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record DenunciaRequest(
        @NotBlank @Size(max = 150) String titulo,
        @NotBlank @Size(max = 2000) String descricao,
        @NotBlank @Size(max = 255) String endereco,
        @NotBlank @Size(max = 120) String bairro,
        @NotBlank @Size(max = 120) String cidade,
        @NotBlank @Size(min = 2, max = 2) String estado,
        @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") BigDecimal latitude,
        @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") BigDecimal longitude,
        @NotNull CategoriaResiduo categoria,
        StatusDenuncia status
) {
}
