package br.com.ecodenuncia.api.service;

import br.com.ecodenuncia.api.dto.DenunciaRequest;
import br.com.ecodenuncia.api.dto.DenunciaResponse;
import br.com.ecodenuncia.api.dto.StatusUpdateRequest;
import br.com.ecodenuncia.api.exception.BusinessException;
import br.com.ecodenuncia.api.exception.ResourceNotFoundException;
import br.com.ecodenuncia.api.model.CategoriaResiduo;
import br.com.ecodenuncia.api.model.Denuncia;
import br.com.ecodenuncia.api.model.Role;
import br.com.ecodenuncia.api.model.StatusDenuncia;
import br.com.ecodenuncia.api.model.Usuario;
import br.com.ecodenuncia.api.repository.CategoriaResiduoRepository;
import br.com.ecodenuncia.api.repository.DenunciaRepository;
import br.com.ecodenuncia.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DenunciaService {

    private final DenunciaRepository denunciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaResiduoRepository categoriaResiduoRepository;

    @Transactional(readOnly = true)
    public Page<DenunciaResponse> listar(Pageable pageable) {
        return denunciaRepository.findAll(pageable).map(DenunciaResponse::from);
    }

    @Transactional(readOnly = true)
    public DenunciaResponse buscarPorId(Long id) {
        Denuncia denuncia = denunciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Denuncia nao encontrada com id: " + id));
        return DenunciaResponse.from(denuncia);
    }

    @Transactional
    public DenunciaResponse criar(DenunciaRequest request) {
        Usuario autenticado = getUsuarioAutenticado();
        CategoriaResiduo categoria = buscarCategoria(request.categoriaResiduoId());

        Denuncia denuncia = Denuncia.builder()
                .titulo(request.titulo())
                .descricao(request.descricao())
                .endereco(request.endereco())
                .bairro(request.bairro())
                .cidade(request.cidade())
                .estado(request.estado())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .status(StatusDenuncia.ABERTA)
                .usuario(autenticado)
                .categoriaResiduo(categoria)
                .build();

        return DenunciaResponse.from(denunciaRepository.save(denuncia));
    }

    @Transactional
    public DenunciaResponse atualizar(Long id, DenunciaRequest request) {
        Denuncia denuncia = denunciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Denuncia nao encontrada com id: " + id));

        Usuario autenticado = getUsuarioAutenticado();
        if (autenticado.getRole() != Role.ADMIN
                && !denuncia.getUsuario().getId().equals(autenticado.getId())) {
            throw new BusinessException("Voce nao tem permissao para alterar esta denuncia");
        }

        CategoriaResiduo categoria = buscarCategoria(request.categoriaResiduoId());

        denuncia.setTitulo(request.titulo());
        denuncia.setDescricao(request.descricao());
        denuncia.setEndereco(request.endereco());
        denuncia.setBairro(request.bairro());
        denuncia.setCidade(request.cidade());
        denuncia.setEstado(request.estado());
        denuncia.setLatitude(request.latitude());
        denuncia.setLongitude(request.longitude());
        denuncia.setCategoriaResiduo(categoria);

        return DenunciaResponse.from(denunciaRepository.save(denuncia));
    }

    @Transactional
    public DenunciaResponse atualizarStatus(Long id, StatusUpdateRequest request) {
        Denuncia denuncia = denunciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Denuncia nao encontrada com id: " + id));
        denuncia.setStatus(request.status());
        return DenunciaResponse.from(denunciaRepository.save(denuncia));
    }

    @Transactional
    public void deletar(Long id) {
        if (!denunciaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Denuncia nao encontrada com id: " + id);
        }
        denunciaRepository.deleteById(id);
    }

    private CategoriaResiduo buscarCategoria(Long id) {
        return categoriaResiduoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria de residuo nao encontrada com id: " + id));
    }

    private Usuario getUsuarioAutenticado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof Usuario u)) {
            throw new BusinessException("Usuario nao autenticado");
        }
        return usuarioRepository.findById(u.getId())
                .orElseThrow(() -> new BusinessException("Usuario autenticado nao encontrado"));
    }
}
