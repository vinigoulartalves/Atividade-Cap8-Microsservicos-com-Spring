package br.com.ecodenuncia.api.dto;

import br.com.ecodenuncia.api.model.CategoriaResiduo;
import br.com.ecodenuncia.api.model.Denuncia;
import br.com.ecodenuncia.api.model.StatusDenuncia;

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
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {
    public static DenunciaResponse from(Denuncia d) {
        return new DenunciaResponse(
                d.getId(),
                d.getTitulo(),
                d.getDescricao(),
                d.getEndereco(),
                d.getBairro(),
                d.getCidade(),
                d.getEstado(),
                d.getLatitude(),
                d.getLongitude(),
                d.getCategoria(),
                d.getStatus(),
                d.getUsuario() != null ? d.getUsuario().getId() : null,
                d.getUsuario() != null ? d.getUsuario().getNome() : null,
                d.getDataCriacao(),
                d.getDataAtualizacao()
        );
    }
}
