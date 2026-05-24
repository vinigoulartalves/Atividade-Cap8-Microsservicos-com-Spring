package br.com.ecodenuncia.service;

import br.com.ecodenuncia.dto.denuncia.DenunciaRequest;
import br.com.ecodenuncia.dto.denuncia.DenunciaResponse;
import br.com.ecodenuncia.dto.usuario.UsuarioResponse;
import br.com.ecodenuncia.exception.ResourceNotFoundException;
import br.com.ecodenuncia.model.Denuncia;
import br.com.ecodenuncia.model.DenunciaStatus;
import br.com.ecodenuncia.model.Usuario;
import br.com.ecodenuncia.repository.DenunciaRepository;
import br.com.ecodenuncia.repository.UsuarioRepository;
import java.time.OffsetDateTime;
import java.util.List;
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

    @Transactional(readOnly = true)
    public List<DenunciaResponse> listar() {
        return denunciaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DenunciaResponse buscarPorId(Long id) {
        return toResponse(findDenuncia(id));
    }

    @Transactional
    public DenunciaResponse criar(DenunciaRequest request, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado nao encontrado"));

        Denuncia denuncia = new Denuncia();
        fillDenuncia(denuncia, request);
        denuncia.setStatus(request.status() == null ? DenunciaStatus.ABERTA : request.status());
        denuncia.setUsuario(usuario);

        return toResponse(denunciaRepository.save(denuncia));
    }

    @Transactional
    public DenunciaResponse atualizar(Long id, DenunciaRequest request) {
        Denuncia denuncia = findDenuncia(id);
        fillDenuncia(denuncia, request);
        if (request.status() != null) {
            denuncia.setStatus(request.status());
        }
        denuncia.setAtualizadoEm(OffsetDateTime.now());
        return toResponse(denunciaRepository.save(denuncia));
    }

    @Transactional
    public void excluir(Long id) {
        Denuncia denuncia = findDenuncia(id);
        denunciaRepository.delete(denuncia);
    }

    private Denuncia findDenuncia(Long id) {
        return denunciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Denuncia nao encontrada"));
    }

    private void fillDenuncia(Denuncia denuncia, DenunciaRequest request) {
        denuncia.setTitulo(request.titulo().trim());
        denuncia.setDescricao(request.descricao().trim());
        denuncia.setEndereco(request.endereco().trim());
        denuncia.setBairro(request.bairro().trim());
        denuncia.setCidade(request.cidade().trim());
        denuncia.setEstado(request.estado().trim().toUpperCase());
        denuncia.setLatitude(request.latitude());
        denuncia.setLongitude(request.longitude());
        denuncia.setCategoria(request.categoria());
    }

    private DenunciaResponse toResponse(Denuncia denuncia) {
        Usuario usuario = denuncia.getUsuario();
        UsuarioResponse usuarioResponse = new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole()
        );

        return new DenunciaResponse(
                denuncia.getId(),
                denuncia.getTitulo(),
                denuncia.getDescricao(),
                denuncia.getEndereco(),
                denuncia.getBairro(),
                denuncia.getCidade(),
                denuncia.getEstado(),
                denuncia.getLatitude(),
                denuncia.getLongitude(),
                denuncia.getCategoria(),
                denuncia.getStatus(),
                usuarioResponse,
                denuncia.getCriadoEm(),
                denuncia.getAtualizadoEm()
        );
    }
}
