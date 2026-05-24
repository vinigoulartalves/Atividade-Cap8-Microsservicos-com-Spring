package br.com.ecodenuncia.api.dto;

import br.com.ecodenuncia.api.model.StatusDenuncia;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de entrada para atualizacao do status de uma denuncia
 * (PATCH /denuncias/{id}/status). Bean Validation: @NotNull.
 */
public record StatusUpdateRequest(
        @NotNull(message = "O status e obrigatorio")
        StatusDenuncia status
) {
}
