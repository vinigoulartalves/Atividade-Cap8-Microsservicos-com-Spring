package br.com.ecodenuncia.api.config.security;

import br.com.ecodenuncia.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementacao de {@link UserDetailsService} que carrega o usuario a partir
 * do banco de dados pelo email (campo usado como "username" da autenticacao).
 * Utilizado pelo {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}
 * configurado em {@link SecurityConfig}.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario nao encontrado com email: " + email));
    }
}
