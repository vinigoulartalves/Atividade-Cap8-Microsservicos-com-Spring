package br.com.ecodenuncia.api.service;

import br.com.ecodenuncia.api.dto.DenunciaRequest;
import br.com.ecodenuncia.api.dto.DenunciaResponse;
import br.com.ecodenuncia.api.dto.DenunciaUpdateRequest;
import br.com.ecodenuncia.api.dto.StatusUpdateRequest;
import br.com.ecodenuncia.api.exception.RecursoNaoEncontradoException;
import br.com.ecodenuncia.api.exception.RegraNegocioException;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class DenunciaService {

    private static final Set<StatusDenuncia> STATUS_RESTRITOS_ADMIN =
            Set.of(StatusDenuncia.RESOLVIDA, StatusDenuncia.CANCELADA);

    private final DenunciaRepository denunciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaResiduoRepository categoriaResiduoRepository;

    @Transactional(readOnly = true)
    public Page<DenunciaResponse> listar(Pageable pageable) {
        return denunciaRepository.findAll(pageable).map(DenunciaResponse::from);
    }

    @Transactional(readOnly = true)
    public DenunciaResponse buscarPorId(Long id) {
        return DenunciaResponse.from(buscarEntidade(id));
    }

    /**
     * Cria uma nova denuncia.
     * - dataCriacao e preenchida automaticamente (vide {@code @PrePersist} em Denuncia).
     * - Status inicial e sempre {@link StatusDenuncia#ABERTA}.
     */
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
    public DenunciaResponse atualizar(Long id, DenunciaUpdateRequest request) {
        Denuncia denuncia = buscarEntidade(id);
        Usuario autenticado = getUsuarioAutenticado();

        if (autenticado.getRole() != Role.ADMIN
                && !denuncia.getUsuario().getId().equals(autenticado.getId())) {
            throw new AccessDeniedException("Voce nao tem permissao para alterar esta denuncia");
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

    /**
     * Atualiza o status de uma denuncia.
     * Regra de negocio:
     * - Transicao para {@link StatusDenuncia#RESOLVIDA} ou {@link StatusDenuncia#CANCELADA}
     *   so e permitida para usuarios com role ADMIN ({@link AccessDeniedException}).
     * - Demais transicoes sao permitidas para o dono da denuncia ou ADMIN.
     */
    @Transactional
    public DenunciaResponse atualizarStatus(Long id, StatusUpdateRequest request) {
        Denuncia denuncia = buscarEntidade(id);
        Usuario autenticado = getUsuarioAutenticado();
        StatusDenuncia novoStatus = request.status();
        boolean isAdmin = autenticado.getRole() == Role.ADMIN;

        if (STATUS_RESTRITOS_ADMIN.contains(novoStatus) && !isAdmin) {
            throw new AccessDeniedException(
                    "Apenas ADMIN pode alterar o status para RESOLVIDA ou CANCELADA");
        }

        if (!isAdmin && !denuncia.getUsuario().getId().equals(autenticado.getId())) {
            throw new AccessDeniedException(
                    "Voce nao tem permissao para alterar o status desta denuncia");
        }

        denuncia.setStatus(novoStatus);
        return DenunciaResponse.from(denunciaRepository.save(denuncia));
    }

    @Transactional
    public void deletar(Long id) {
        if (!denunciaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Denuncia nao encontrada com id: " + id);
        }
        denunciaRepository.deleteById(id);
    }

    private Denuncia buscarEntidade(Long id) {
        return denunciaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Denuncia nao encontrada com id: " + id));
    }

    private CategoriaResiduo buscarCategoria(Long id) {
        return categoriaResiduoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria de residuo nao encontrada com id: " + id));
    }

    private Usuario getUsuarioAutenticado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof Usuario u)) {
            throw new RegraNegocioException("Usuario nao autenticado");
        }
        return usuarioRepository.findById(u.getId())
                .orElseThrow(() -> new RegraNegocioException("Usuario autenticado nao encontrado"));
    }
}
