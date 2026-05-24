package br.com.ecodenuncia.api.service;

import br.com.ecodenuncia.api.config.security.TokenService;
import br.com.ecodenuncia.api.dto.AuthResponse;
import br.com.ecodenuncia.api.dto.LoginRequest;
import br.com.ecodenuncia.api.dto.RegisterRequest;
import br.com.ecodenuncia.api.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Orquestra o fluxo de autenticacao: delega o cadastro ao UsuarioService,
 * realiza o login via AuthenticationManager e emite o JWT via TokenService.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioService usuarioService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        Usuario usuario = usuarioService.cadastrar(request);
        return gerarResposta(usuario);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );
        Usuario usuario = usuarioService.buscarPorEmail(request.email());
        return gerarResposta(usuario);
    }

    private AuthResponse gerarResposta(Usuario usuario) {
        String token = tokenService.generateToken(usuario, Map.of("role", usuario.getRole().name()));
        return AuthResponse.bearer(token, usuario.getId(), usuario.getNome(),
                usuario.getEmail(), usuario.getRole());
    }
}
