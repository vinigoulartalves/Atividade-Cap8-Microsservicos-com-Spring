package com.ecodenuncia.dto.denuncia;

import com.ecodenuncia.model.CategoriaResiduo;
import com.ecodenuncia.model.Denuncia;
import com.ecodenuncia.model.StatusDenuncia;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DenunciaResponse(
        Long id,
        String titulo,
        String descricao,
        String endereco,
        String bairro,
        String cidade,
        String estado,
        BigDecimal latitude,
        BigDecimal longitude,
        CategoriaResiduo categoria,
        StatusDenuncia status,
        Long usuarioId,
        String usuarioNome,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
    public static DenunciaResponse from(Denuncia denuncia) {
        return new DenunciaResponse(
                denuncia.getId(),
                denuncia.getTitulo(),
                denuncia.getDescricao(),
                denuncia.getEndereco(),
                denuncia.getBairro(),
                denuncia.getCidade(),
                denuncia.getEstado(),
                denuncia.getLatitude(),
                denuncia.getLongitude(),
                denuncia.getCategoria(),
                denuncia.getStatus(),
                denuncia.getUsuario().getId(),
                denuncia.getUsuario().getNome(),
                denuncia.getCriadoEm(),
                denuncia.getAtualizadoEm()
        );
    }
}
