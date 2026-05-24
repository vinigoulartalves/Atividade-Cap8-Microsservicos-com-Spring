package com.ecodenuncia.api.dto.denuncia;

import com.ecodenuncia.api.model.enums.CategoriaResiduo;
import com.ecodenuncia.api.model.enums.StatusDenuncia;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DenunciaResponseDto(
        Long id,
        String titulo,
        String descricao,
        String endereco,
        String bairro,
        String cidade,
        String estado,
        BigDecimal latitude,
        BigDecimal longitude,
        CategoriaResiduo categoriaResiduo,
        StatusDenuncia status,
        Long usuarioId,
        String usuarioNome,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {
}
