package com.miproyecto.backend.config;

import tools.jackson.databind.ObjectMapper;
import com.miproyecto.backend.auth.service.UsuarioDetailsService;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
    @Bean public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService uds, org.springframework.security.crypto.password.PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(uds); provider.setPasswordEncoder(encoder); return provider;
    }
    @Bean public AuthenticationManager authenticationManager(org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration config) throws Exception { return config.getAuthenticationManager(); }
    @Bean public SecretKey jwtSecretKey(@Value("${app.jwt.secret}") String secret) {
        byte[] bytes;
        try { bytes = Base64.getDecoder().decode(secret); } catch (IllegalArgumentException e) { bytes = secret.getBytes(java.nio.charset.StandardCharsets.UTF_8); }
        if (bytes.length < 32) throw new IllegalStateException("JWT_SECRET debe contener al menos 256 bits");
        return new SecretKeySpec(bytes, "HmacSHA256");
    }
    @Bean public JwtEncoder jwtEncoder(SecretKey key) { return new NimbusJwtEncoder(new ImmutableSecret<>(key)); }
    @Bean public JwtDecoder jwtDecoder(SecretKey key) { return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build(); }
    @Bean public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter roles = new JwtGrantedAuthoritiesConverter(); roles.setAuthoritiesClaimName("roles"); roles.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter c = new JwtAuthenticationConverter(); c.setJwtGrantedAuthoritiesConverter(roles); return c;
    }
    @Bean public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationConverter jwtConverter,
            ObjectMapper mapper) throws Exception {
        AuthenticationEntryPoint entry = (request, response, ex) -> writeError(mapper, response, 401, "Unauthorized", "Autenticación requerida", request.getRequestURI());
        AccessDeniedHandler denied = (request, response, ex) -> writeError(mapper, response, 403, "Forbidden", "Acceso denegado", request.getRequestURI());
        http.csrf(c -> c.disable()).cors(c -> c.configurationSource(corsConfigurationSource()))
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(e -> e.authenticationEntryPoint(entry).accessDeniedHandler(denied))
            .authorizeHttpRequests(a -> a
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/tipografias", "/api/tipografias/**", "/api/artistas", "/api/artistas/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                .requestMatchers("/api/usuarios/me", "/api/usuarios/me/**").authenticated()
                .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/tipografias/**", "/api/artistas/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/tipografias/**", "/api/artistas/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/tipografias/**", "/api/artistas/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .oauth2ResourceServer(o -> o.jwt(j -> j.jwtAuthenticationConverter(jwtConverter)).authenticationEntryPoint(entry).accessDeniedHandler(denied));
        return http.build();
    }
    @Bean public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration c = new CorsConfiguration(); c.setAllowedOrigins(java.util.List.of("http://localhost:4200"));
        c.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        c.setAllowedHeaders(java.util.List.of("Authorization", "Content-Type")); c.setExposedHeaders(java.util.List.of("Authorization"));
        UrlBasedCorsConfigurationSource s = new UrlBasedCorsConfigurationSource(); s.registerCorsConfiguration("/**", c); return s;
    }
    private static void writeError(ObjectMapper mapper, jakarta.servlet.http.HttpServletResponse response, int status, String error, String message, String path) throws java.io.IOException {
        response.setStatus(status); response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getOutputStream(), Map.of("timestamp", Instant.now().toString(), "status", status, "error", error, "message", message, "path", path));
    }
}
