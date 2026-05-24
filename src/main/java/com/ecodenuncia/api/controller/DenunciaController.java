package com.ecodenuncia.api.controller;

import com.ecodenuncia.api.dto.denuncia.DenunciaRequestDto;
import com.ecodenuncia.api.dto.denuncia.DenunciaResponseDto;
import com.ecodenuncia.api.service.DenunciaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/denuncias")
public class DenunciaController {

    private final DenunciaService denunciaService;

    public DenunciaController(DenunciaService denunciaService) {
        this.denunciaService = denunciaService;
    }

    @GetMapping
    public List<DenunciaResponseDto> listarTodas() {
        return denunciaService.listarTodas();
    }

    @GetMapping("/{id}")
    public DenunciaResponseDto buscarPorId(@PathVariable Long id) {
        return denunciaService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DenunciaResponseDto criar(@Valid @RequestBody DenunciaRequestDto request) {
        return denunciaService.criar(request);
    }

    @PutMapping("/{id}")
    public DenunciaResponseDto atualizar(@PathVariable Long id, @Valid @RequestBody DenunciaRequestDto request) {
        return denunciaService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        denunciaService.deletar(id);
    }
}
