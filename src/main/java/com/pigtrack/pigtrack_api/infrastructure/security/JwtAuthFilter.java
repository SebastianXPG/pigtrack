package com.pigtrack.pigtrack_api.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.http.HttpMethod; // Importar HttpMethod para la verificación del método

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // --- INICIO DE LOGGING PARA DIAGNÓSTICO ---
        logger.info("JwtAuthFilter: Procesando solicitud para URL: {} y Método: {}", request.getRequestURI(), request.getMethod());
        // --- FIN DE LOGGING PARA DIAGNÓSTICO ---

        // Esta doble verificación debería ser redundante si webSecurityCustomizer() funciona correctamente,
        // pero la mantenemos para depuración y seguridad.
        if (HttpMethod.POST.name().equals(request.getMethod()) && "/api/usuarios".equals(request.getRequestURI())) {
            logger.info("JwtAuthFilter: Ignorando solicitud POST a /api/usuarios según regla interna del filtro.");
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String userEmail = null; // Usaremos 'userEmail' en lugar de 'username' para mayor claridad
        String token = null;

        // 1. Verificar si el encabezado de autorización está presente y tiene el formato esperado
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Si no hay token o el formato es incorrecto, simplemente pasa la solicitud al siguiente filtro.
            // Spring Security se encargará de denegar el acceso si la ruta requiere autenticación.
            // No respondemos con 401 aquí directamente, para permitir que otras reglas de seguridad se apliquen.
            logger.debug("JwtAuthFilter: No se proporcionó token Bearer o formato inválido para URL: {}. Continuando la cadena de filtros.", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        token = authHeader.substring(7); // Extrae el token JWT

        // 2. Extraer el email del token
        try {
            userEmail = jwtService.extraerEmail(token);
            logger.debug("JwtAuthFilter: Token extraído. Email del usuario: {}", userEmail);
        } catch (Exception e) {
            // Si hay un error al extraer el email (token inválido, expirado, etc.),
            // loguea el error y pasa al siguiente filtro. La autenticación fallará más adelante.
            logger.warn("JwtAuthFilter: Error al extraer email del token para URL: {}. Error: {}", request.getRequestURI(), e.getMessage());
            // No enviamos 401 aquí para permitir que Spring Security maneje la excepción de autenticación de forma más consistente.
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Si se extrajo un email y no hay autenticación actual en el contexto de seguridad
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            try {
                // Cargar los detalles del usuario usando el email extraído del token
                userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            } catch (Exception e) {
                logger.warn("JwtAuthFilter: No se pudo cargar UserDetails para el email {}. Error: {}", userEmail, e.getMessage());
                filterChain.doFilter(request, response);
                return;
            }

            // 4. Validar el token y el usuario
            // Se valida el token con JwtService, y se verifica que el email del token coincida con el UserDetails cargado.
            if (jwtService.validarToken(token) && userDetails.getUsername().equals(userEmail)) {
                // Si el token es válido y el usuario existe, crear un objeto de autenticación
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // La contraseña ya no es necesaria aquí para la autenticación de JWT
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Establecer el objeto de autenticación en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("JwtAuthFilter: Usuario {} autenticado correctamente y establecido en el SecurityContext.", userEmail);
            } else {
                logger.warn("JwtAuthFilter: Token inválido o email no coincide para usuario {}.", userEmail);
                // No enviamos 401 aquí, se dejará que Spring Security lo maneje.
            }
        } else if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            logger.debug("JwtAuthFilter: Ya existe autenticación en el contexto para URL: {}. No se procesa el token JWT.", request.getRequestURI());
        } else {
            logger.warn("JwtAuthFilter: Email nulo después de extracción de token o problema desconocido para URL: {}.", request.getRequestURI());
        }

        // Continúa con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
