package br.com.ecodenuncia.api.controller;

import br.com.ecodenuncia.api.dto.DenunciaRequest;
import br.com.ecodenuncia.api.dto.DenunciaResponse;
import br.com.ecodenuncia.api.dto.StatusUpdateRequest;
import br.com.ecodenuncia.api.service.DenunciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/denuncias")
@RequiredArgsConstructor
public class DenunciaController {

    private final DenunciaService denunciaService;

    @GetMapping
    public ResponseEntity<Page<DenunciaResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(denunciaService.listar(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DenunciaResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(denunciaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<DenunciaResponse> criar(
            @Valid @RequestBody DenunciaRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        DenunciaResponse response = denunciaService.criar(request);
        URI location = uriBuilder.path("/denuncias/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DenunciaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody DenunciaRequest request
    ) {
        return ResponseEntity.ok(denunciaService.atualizar(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<DenunciaResponse> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request
    ) {
        return ResponseEntity.ok(denunciaService.atualizarStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        denunciaService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
