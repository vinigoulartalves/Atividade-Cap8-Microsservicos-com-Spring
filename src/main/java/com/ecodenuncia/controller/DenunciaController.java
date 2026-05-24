package com.ecodenuncia.controller;

import com.ecodenuncia.dto.denuncia.DenunciaRequest;
import com.ecodenuncia.dto.denuncia.DenunciaResponse;
import com.ecodenuncia.service.DenunciaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/denuncias")
public class DenunciaController {

    private final DenunciaService denunciaService;

    public DenunciaController(DenunciaService denunciaService) {
        this.denunciaService = denunciaService;
    }

    @GetMapping
    public List<DenunciaResponse> listar() {
        return denunciaService.listarTodas();
    }

    @GetMapping("/{id}")
    public DenunciaResponse buscar(@PathVariable Long id) {
        return denunciaService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DenunciaResponse criar(@Valid @RequestBody DenunciaRequest request) {
        return denunciaService.criar(request);
    }

    @PutMapping("/{id}")
    public DenunciaResponse atualizar(@PathVariable Long id, @Valid @RequestBody DenunciaRequest request) {
        return denunciaService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        denunciaService.excluir(id);
    }
}
