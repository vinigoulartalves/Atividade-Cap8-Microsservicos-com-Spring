package br.com.ecodenuncia.api.controller;

import br.com.ecodenuncia.api.dto.CategoriaResiduoRequest;
import br.com.ecodenuncia.api.dto.CategoriaResiduoResponse;
import br.com.ecodenuncia.api.service.CategoriaResiduoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaResiduoController {

    private final CategoriaResiduoService categoriaResiduoService;

    @GetMapping
    public ResponseEntity<List<CategoriaResiduoResponse>> listar() {
        return ResponseEntity.ok(categoriaResiduoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResiduoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaResiduoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaResiduoResponse> criar(
            @Valid @RequestBody CategoriaResiduoRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        CategoriaResiduoResponse response = categoriaResiduoService.criar(request);
        URI location = uriBuilder.path("/categorias/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResiduoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaResiduoRequest request
    ) {
        return ResponseEntity.ok(categoriaResiduoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaResiduoService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
