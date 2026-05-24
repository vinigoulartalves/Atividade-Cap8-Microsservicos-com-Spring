package br.com.ecodenuncia.api.dto;

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
        StatusDenuncia status,
        LocalDateTime dataCriacao,
        Long usuarioId,
        String usuarioNome,
        Long categoriaResiduoId,
        String categoriaResiduoNome,
        String categoriaResiduoDescricao
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
                d.getStatus(),
                d.getDataCriacao(),
                d.getUsuario() != null ? d.getUsuario().getId() : null,
                d.getUsuario() != null ? d.getUsuario().getNome() : null,
                d.getCategoriaResiduo() != null ? d.getCategoriaResiduo().getId() : null,
                d.getCategoriaResiduo() != null ? d.getCategoriaResiduo().getNome() : null,
                d.getCategoriaResiduo() != null ? d.getCategoriaResiduo().getDescricao() : null
        );
    }
}
