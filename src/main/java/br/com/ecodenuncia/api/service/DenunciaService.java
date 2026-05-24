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
        return DenunciaResponse.from(buscarEntidade(id));
    }

    /**
     * Cria uma nova denuncia.
     * - {@code dataCriacao} e preenchida automaticamente (via {@code @PrePersist} em Denuncia).
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

    /**
     * Atualiza os dados de uma denuncia. USER comum so pode atualizar a propria
     * denuncia; ADMIN pode atualizar qualquer.
     */
    @Transactional
    public DenunciaResponse atualizar(Long id, DenunciaUpdateRequest request) {
        Denuncia denuncia = buscarEntidade(id);
        Usuario autenticado = getUsuarioAutenticado();
        validarPermissaoEdicao(denuncia, autenticado, "alterar");

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
     * Atualiza apenas o status da denuncia.
     * A protecao de role ADMIN-only e feita no SecurityConfig
     * ({@code PATCH /denuncias/*\/status} -> hasRole("ADMIN")).
     */
    @Transactional
    public DenunciaResponse atualizarStatus(Long id, StatusUpdateRequest request) {
        Denuncia denuncia = buscarEntidade(id);
        denuncia.setStatus(request.status());
        return DenunciaResponse.from(denunciaRepository.save(denuncia));
    }

    /**
     * Remove uma denuncia. USER comum so pode remover a propria denuncia;
     * ADMIN pode remover qualquer.
     */
    @Transactional
    public void deletar(Long id) {
        Denuncia denuncia = buscarEntidade(id);
        Usuario autenticado = getUsuarioAutenticado();
        validarPermissaoEdicao(denuncia, autenticado, "deletar");
        denunciaRepository.delete(denuncia);
    }

    // ----------------------------------------------------------------------

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

    private void validarPermissaoEdicao(Denuncia denuncia, Usuario autenticado, String acao) {
        if (autenticado.getRole() != Role.ADMIN
                && !denuncia.getUsuario().getId().equals(autenticado.getId())) {
            throw new AccessDeniedException(
                    "Voce nao tem permissao para " + acao + " esta denuncia");
        }
    }
}
