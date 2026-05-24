package com.ecodenuncia.api.service;

import com.ecodenuncia.api.dto.denuncia.DenunciaRequestDto;
import com.ecodenuncia.api.dto.denuncia.DenunciaResponseDto;
import com.ecodenuncia.api.exception.BusinessException;
import com.ecodenuncia.api.exception.ResourceNotFoundException;
import com.ecodenuncia.api.model.entity.Denuncia;
import com.ecodenuncia.api.model.entity.Usuario;
import com.ecodenuncia.api.model.enums.Role;
import com.ecodenuncia.api.repository.DenunciaRepository;
import com.ecodenuncia.api.repository.UsuarioRepository;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DenunciaService {

    private final DenunciaRepository denunciaRepository;
    private final UsuarioRepository usuarioRepository;

    public DenunciaService(DenunciaRepository denunciaRepository, UsuarioRepository usuarioRepository) {
        this.denunciaRepository = denunciaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<DenunciaResponseDto> listarTodas() {
        return denunciaRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public DenunciaResponseDto buscarPorId(Long id) {
        Denuncia denuncia = denunciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Denúncia não encontrada."));
        return toResponseDto(denuncia);
    }

    @Transactional
    public DenunciaResponseDto criar(DenunciaRequestDto dto) {
        Usuario usuarioLogado = getUsuarioLogado();
        Denuncia denuncia = new Denuncia();
        mapRequestToEntity(dto, denuncia);
        denuncia.setUsuario(usuarioLogado);
        Denuncia saved = denunciaRepository.save(denuncia);
        return toResponseDto(saved);
    }

    @Transactional
    public DenunciaResponseDto atualizar(Long id, DenunciaRequestDto dto) {
        Denuncia denuncia = denunciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Denúncia não encontrada."));
        validarPermissaoEdicao(denuncia);
        mapRequestToEntity(dto, denuncia);
        Denuncia saved = denunciaRepository.save(denuncia);
        return toResponseDto(saved);
    }

    @Transactional
    public void deletar(Long id) {
        Denuncia denuncia = denunciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Denúncia não encontrada."));
        denunciaRepository.delete(denuncia);
    }

    private void mapRequestToEntity(DenunciaRequestDto dto, Denuncia denuncia) {
        denuncia.setTitulo(dto.titulo());
        denuncia.setDescricao(dto.descricao());
        denuncia.setEndereco(dto.endereco());
        denuncia.setBairro(dto.bairro());
        denuncia.setCidade(dto.cidade());
        denuncia.setEstado(dto.estado().toUpperCase());
        denuncia.setLatitude(dto.latitude());
        denuncia.setLongitude(dto.longitude());
        denuncia.setCategoriaResiduo(dto.categoriaResiduo());
        denuncia.setStatus(dto.status());
    }

    private DenunciaResponseDto toResponseDto(Denuncia denuncia) {
        return new DenunciaResponseDto(
                denuncia.getId(),
                denuncia.getTitulo(),
                denuncia.getDescricao(),
                denuncia.getEndereco(),
                denuncia.getBairro(),
                denuncia.getCidade(),
                denuncia.getEstado(),
                denuncia.getLatitude(),
                denuncia.getLongitude(),
                denuncia.getCategoriaResiduo(),
                denuncia.getStatus(),
                denuncia.getUsuario().getId(),
                denuncia.getUsuario().getNome(),
                denuncia.getDataCriacao(),
                denuncia.getDataAtualizacao()
        );
    }

    private Usuario getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new BusinessException("Usuário não autenticado.");
        }

        String email = authentication.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário autenticado não encontrado."));
    }

    private void validarPermissaoEdicao(Denuncia denuncia) {
        Usuario usuarioLogado = getUsuarioLogado();
        boolean isAdmin = usuarioLogado.getRole() == Role.ADMIN;
        boolean isOwner = denuncia.getUsuario().getId().equals(usuarioLogado.getId());

        if (!isAdmin && !isOwner) {
            throw new BusinessException("Você não tem permissão para atualizar esta denúncia.");
        }
    }
}
