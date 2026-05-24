package br.com.ecodenuncia.api.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuracao central do Spring Security.
 *
 * <p>Regras de autorizacao:</p>
 * <ul>
 *   <li>Publicos: /auth/**, GET /denuncias/**, GET /categorias/**</li>
 *   <li>Autenticado: POST/PUT/DELETE /denuncias</li>
 *   <li>ADMIN: PATCH /denuncias/{id}/status, POST/PUT/DELETE /categorias</li>
 * </ul>
 *
 * <p>Sessao stateless (somente JWT). Senhas criptografadas com BCrypt.</p>
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // -------- Publicos --------
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll()
                        .requestMatchers(HttpMethod.GET, "/denuncias", "/denuncias/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categorias", "/categorias/*").permitAll()

                        // -------- Apenas ADMIN --------
                        .requestMatchers(HttpMethod.PATCH, "/denuncias/*/status").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,   "/categorias", "/categorias/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/categorias/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/categorias/*").hasRole("ADMIN")

                        // -------- Demais (POST/PUT/DELETE /denuncias, etc.) --------
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
