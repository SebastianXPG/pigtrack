package com.pigtrack.pigtrack_api.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        String method = request.getMethod();

        System.out.println("🔍 Petición a: " + path + " con método: " + method);

        // 🔓 Rutas públicas que no requieren token
        boolean rutaPublica =
                (path.equals("/api/auth/login") && method.equals("POST")) ||  // 👈 FIX AQUI
                        (path.equals("/api/usuarios") && method.equals("POST"));      // 👈 FIX AQUI

        if (rutaPublica) {
            System.out.println("✅ Ruta pública permitida sin token.");
            filterChain.doFilter(request, response);
            return;
        }

        // 🔒 Verifica token para el resto
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("⛔ Token faltante o mal formado.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token no proporcionado o inválido");
            return;
        }

        String jwt = authHeader.substring(7);
        String email = jwtService.extraerEmail(jwt); // usar email como identificador

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtService.validarToken(jwt)) {
                System.out.println("✅ Token válido para: " + email);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
//                System.out.println("⛔ Token inválido");
                System.out.println("⛔ Token inválido: " + jwt);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token inválido");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
