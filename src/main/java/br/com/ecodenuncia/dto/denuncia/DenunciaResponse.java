package br.com.ecodenuncia.dto.denuncia;

import br.com.ecodenuncia.dto.usuario.UsuarioResponse;
import br.com.ecodenuncia.model.CategoriaResiduo;
import br.com.ecodenuncia.model.DenunciaStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

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
        DenunciaStatus status,
        UsuarioResponse usuario,
        OffsetDateTime criadoEm,
        OffsetDateTime atualizadoEm
) {
}
