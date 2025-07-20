package com.pigtrack.pigtrack_api.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // Importar SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // Importar para csrf.disable()

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService; // Inyectado para el AuthenticationProvider

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomUserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Configuración de CORS
                .cors(cors -> cors
                        .configurationSource(request -> {
                            CorsConfiguration config = new CorsConfiguration();
                            // Permite todos los orígenes. En producción, especifica los dominios exactos.
                            config.setAllowedOrigins(List.of("*"));
                            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                            config.setAllowedHeaders(List.of("*")); // Permite todos los headers, incluyendo Authorization
                            config.setAllowCredentials(true); // Permitir el envío de cookies o credenciales
                            config.setMaxAge(3600L); // Cache preflight requests for 1 hour
                            return config;
                        })
                )
                // Deshabilitar CSRF para APIs REST (ya que JWT protege contra CSRF)
                .csrf(AbstractHttpConfigurer::disable)
                // Configuración de las reglas de autorización HTTP
                .authorizeHttpRequests(auth -> auth
                        // Permite el acceso sin autenticación a la ruta de registro de usuarios
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                        // Permite el acceso sin autenticación a las rutas de autenticación (login, etc.)
                        .requestMatchers("/api/auth/**").permitAll()
                        // Aquí puedes añadir otras rutas que no requieran autenticación, por ejemplo, Swagger UI
                        // .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // Todas las demás solicitudes requieren autenticación
                        .anyRequest().authenticated()
                )
                // Configuración de la gestión de sesiones como STATELESS (sin estado) para JWT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Añadir el filtro JWT antes del filtro de autenticación de usuario y contraseña de Spring Security
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean para el proveedor de autenticación
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // Usa tu CustomUserDetailsService
        provider.setPasswordEncoder(passwordEncoder());      // Usa tu PasswordEncoder
        return provider;
    }

    // Bean para el AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Bean para el codificador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}