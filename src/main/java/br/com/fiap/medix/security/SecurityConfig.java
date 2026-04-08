package br.com.fiap.medix.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Configura a corrente de filtros de segurança e as permissões de acesso da API
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityFilter securityFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                // Habilita o compartilhamento de recursos (CORS) para integração com o Angular
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // 1. ACESSO PÚBLICO: Documentação e Autenticação
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                        // 2. PROCEDURES E FUNCTIONS (EXCLUSIVO ADMIN):
                        // Lógicas pesadas e relatórios analíticos do Oracle
                        .requestMatchers("/agendamentos/relatorio-navegacao").hasRole("ADMIN")
                        .requestMatchers("/agendamentos/historico-json/**").hasRole("ADMIN")
                        .requestMatchers("/unidades/duracao-total/**").hasRole("ADMIN")

                        // 3. UNIDADES DE SAÚDE (ADMIN + COLABORADOR):
                        // Aqui os colaboradores podem fazer POST, PUT e DELETE normalmente
                        // O GET continua público para qualquer um ver onde tem unidade
                        .requestMatchers(HttpMethod.GET, "/unidades/**").permitAll()
                        .requestMatchers("/unidades/**").hasAnyRole("ADMIN", "COLABORADOR")

                        // 4. ADMINISTRAÇÃO GERAL: Rotas de gestão de usuários (Auditoria)
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 5. OPERACIONAL: Agendamentos exigem apenas login (Paciente/Médico/Admin)
                        .requestMatchers("/agendamentos/**").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Define as origens e métodos permitidos para requisições externas do Front-end
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Configura o codificador de senhas (NoOp apenas para facilitação dos testes da Sprint)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // Expõe o gerenciador de autenticação necessário para o processo de login via Token
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}