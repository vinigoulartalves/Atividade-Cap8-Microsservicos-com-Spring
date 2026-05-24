package br.com.ecodenuncia.controller;

import br.com.ecodenuncia.dto.denuncia.DenunciaRequest;
import br.com.ecodenuncia.dto.denuncia.DenunciaResponse;
import br.com.ecodenuncia.service.DenunciaService;
import jakarta.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/denuncias")
public class DenunciaController {

    private final DenunciaService denunciaService;

    public DenunciaController(DenunciaService denunciaService) {
        this.denunciaService = denunciaService;
    }

    @GetMapping
    public ResponseEntity<List<DenunciaResponse>> listar() {
        return ResponseEntity.ok(denunciaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DenunciaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(denunciaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<DenunciaResponse> criar(@Valid @RequestBody DenunciaRequest request, Principal principal) {
        DenunciaResponse response = denunciaService.criar(request, principal.getName());
        return ResponseEntity.created(URI.create("/api/denuncias/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DenunciaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody DenunciaRequest request
    ) {
        return ResponseEntity.ok(denunciaService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        denunciaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
