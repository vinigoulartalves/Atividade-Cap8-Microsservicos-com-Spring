package br.com.ecodenuncia.api.dto;

import br.com.ecodenuncia.api.model.StatusDenuncia;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(
        @NotNull(message = "O status e obrigatorio")
        StatusDenuncia status
) {
}
