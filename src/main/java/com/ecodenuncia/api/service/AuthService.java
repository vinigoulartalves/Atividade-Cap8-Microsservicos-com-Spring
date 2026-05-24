package com.ecodenuncia.api.service;

import com.ecodenuncia.api.dto.auth.AuthResponseDto;
import com.ecodenuncia.api.dto.auth.CadastroRequestDto;
import com.ecodenuncia.api.dto.auth.LoginRequestDto;
import com.ecodenuncia.api.exception.BusinessException;
import com.ecodenuncia.api.model.entity.Usuario;
import com.ecodenuncia.api.repository.UsuarioRepository;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponseDto cadastrar(CadastroRequestDto dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new BusinessException("Já existe um usuário cadastrado com este e-mail.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setRole(dto.role());

        Usuario saved = usuarioRepository.save(usuario);
        String token = jwtService.generateToken(
                Map.of("role", saved.getRole().name(), "userId", saved.getId()),
                saved
        );

        return new AuthResponseDto(
                token,
                "Bearer",
                saved.getId(),
                saved.getNome(),
                saved.getEmail(),
                saved.getRole()
        );
    }

    public AuthResponseDto login(LoginRequestDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.senha())
        );

        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new BusinessException("Credenciais inválidas."));

        String token = jwtService.generateToken(
                Map.of("role", usuario.getRole().name(), "userId", usuario.getId()),
                usuario
        );

        return new AuthResponseDto(
                token,
                "Bearer",
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole()
        );
    }
}
