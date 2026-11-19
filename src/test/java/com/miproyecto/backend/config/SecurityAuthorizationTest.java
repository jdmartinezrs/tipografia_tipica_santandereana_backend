package com.miproyecto.backend.config;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@WebMvcTest(SecurityAuthorizationTest.ProbeController.class)
@Import({SecurityConfig.class, PasswordConfig.class})
@TestPropertySource(properties = "app.jwt.secret=MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=")
class SecurityAuthorizationTest {
    @Autowired MockMvc mvc;
    @MockitoBean UserDetailsService userDetailsService;

    @RestController
    static class ProbeController {
        @GetMapping("/api/usuarios/me") String me() { return "ok"; }
        @GetMapping("/api/usuarios") String users() { return "admin"; }
    }

    @Test void sinTokenResponde401() throws Exception {
        mvc.perform(get("/api/usuarios/me")).andExpect(status().isUnauthorized());
    }
    @Test void clienteEnRutaAdminResponde403() throws Exception {
        mvc.perform(get("/api/usuarios").with(jwt().authorities(List.of(new SimpleGrantedAuthority("ROLE_CLIENTE")))))
                .andExpect(status().isForbidden());
    }
}
