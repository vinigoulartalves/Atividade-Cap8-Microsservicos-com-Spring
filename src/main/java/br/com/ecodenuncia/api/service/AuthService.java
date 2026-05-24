package br.com.ecodenuncia.api.service;

import br.com.ecodenuncia.api.config.security.JwtService;
import br.com.ecodenuncia.api.dto.AuthResponse;
import br.com.ecodenuncia.api.dto.LoginRequest;
import br.com.ecodenuncia.api.dto.RegisterRequest;
import br.com.ecodenuncia.api.exception.BusinessException;
import br.com.ecodenuncia.api.model.Role;
import br.com.ecodenuncia.api.model.Usuario;
import br.com.ecodenuncia.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email ja cadastrado");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .role(Role.USER)
                .build();

        Usuario saved = usuarioRepository.save(usuario);

        String token = jwtService.generateToken(saved, Map.of("role", saved.getRole().name()));
        return AuthResponse.bearer(token, saved.getId(), saved.getNome(), saved.getEmail(), saved.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Usuario nao encontrado"));

        String token = jwtService.generateToken(usuario, Map.of("role", usuario.getRole().name()));
        return AuthResponse.bearer(token, usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole());
    }
}
