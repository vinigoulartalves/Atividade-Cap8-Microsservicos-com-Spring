package com.ecodenuncia.service;

import com.ecodenuncia.dto.denuncia.DenunciaRequest;
import com.ecodenuncia.dto.denuncia.DenunciaResponse;
import com.ecodenuncia.exception.AccessDeniedException;
import com.ecodenuncia.exception.ResourceNotFoundException;
import com.ecodenuncia.model.Denuncia;
import com.ecodenuncia.model.Role;
import com.ecodenuncia.model.StatusDenuncia;
import com.ecodenuncia.model.Usuario;
import com.ecodenuncia.repository.DenunciaRepository;
import com.ecodenuncia.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DenunciaService {

    private final DenunciaRepository denunciaRepository;
    private final UsuarioRepository usuarioRepository;

    public DenunciaService(DenunciaRepository denunciaRepository, UsuarioRepository usuarioRepository) {
        this.denunciaRepository = denunciaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<DenunciaResponse> listarTodas() {
        return denunciaRepository.findAll().stream()
                .map(DenunciaResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public DenunciaResponse buscarPorId(Long id) {
        return DenunciaResponse.from(buscarEntidade(id));
    }

    @Transactional
    public DenunciaResponse criar(DenunciaRequest request) {
        Usuario usuario = obterUsuarioAutenticado();
        Denuncia denuncia = new Denuncia();
        preencherDenuncia(denuncia, request);
        denuncia.setStatus(request.status() != null ? request.status() : StatusDenuncia.ABERTA);
        denuncia.setUsuario(usuario);
        return DenunciaResponse.from(denunciaRepository.save(denuncia));
    }

    @Transactional
    public DenunciaResponse atualizar(Long id, DenunciaRequest request) {
        Denuncia denuncia = buscarEntidade(id);
        validarPermissao(denuncia);
        preencherDenuncia(denuncia, request);
        if (request.status() != null) {
            denuncia.setStatus(request.status());
        }
        return DenunciaResponse.from(denunciaRepository.save(denuncia));
    }

    @Transactional
    public void excluir(Long id) {
        Denuncia denuncia = buscarEntidade(id);
        validarPermissao(denuncia);
        denunciaRepository.delete(denuncia);
    }

    private Denuncia buscarEntidade(Long id) {
        return denunciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Denúncia não encontrada com id: " + id));
    }

    private void preencherDenuncia(Denuncia denuncia, DenunciaRequest request) {
        denuncia.setTitulo(request.titulo());
        denuncia.setDescricao(request.descricao());
        denuncia.setEndereco(request.endereco());
        denuncia.setBairro(request.bairro());
        denuncia.setCidade(request.cidade());
        denuncia.setEstado(request.estado().toUpperCase());
        denuncia.setLatitude(request.latitude());
        denuncia.setLongitude(request.longitude());
        denuncia.setCategoria(request.categoria());
    }

    private void validarPermissao(Denuncia denuncia) {
        Usuario usuario = obterUsuarioAutenticado();
        if (usuario.getRole() == Role.ADMIN) {
            return;
        }
        if (!denuncia.getUsuario().getId().equals(usuario.getId())) {
            throw new AccessDeniedException("Você não tem permissão para alterar esta denúncia");
        }
    }

    private Usuario obterUsuarioAutenticado() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário autenticado não encontrado"));
    }
}
