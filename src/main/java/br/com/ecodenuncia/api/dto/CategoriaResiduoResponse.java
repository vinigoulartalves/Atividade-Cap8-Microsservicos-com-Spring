package br.com.ecodenuncia.api.dto;

import br.com.ecodenuncia.api.model.CategoriaResiduo;

/**
 * DTO de saida para exibicao de Categoria de Residuo.
 */
public record CategoriaResiduoResponse(
        Long id,
        String nome,
        String descricao
) {

    public static CategoriaResiduoResponse from(CategoriaResiduo c) {
        return new CategoriaResiduoResponse(c.getId(), c.getNome(), c.getDescricao());
    }
}
