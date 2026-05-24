package br.com.ecodenuncia.api.service;

import br.com.ecodenuncia.api.dto.RegisterRequest;
import br.com.ecodenuncia.api.exception.RecursoNaoEncontradoException;
import br.com.ecodenuncia.api.exception.RegraNegocioException;
import br.com.ecodenuncia.api.model.Role;
import br.com.ecodenuncia.api.model.Usuario;
import br.com.ecodenuncia.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuario nao encontrado com id: " + id));
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuario nao encontrado com email: " + email));
    }

    /**
     * Cadastra um novo usuario com role USER por padrao.
     * Lanca {@link RegraNegocioException} se o email ja estiver em uso.
     */
    @Transactional
    public Usuario cadastrar(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RegraNegocioException("Email ja cadastrado: " + request.email());
        }

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .role(Role.USER)
                .build();

        return usuarioRepository.save(usuario);
    }
}
