package com.pigtrack.pigtrack_api.infrastructure.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "clave_secreta_super_segura"; // cámbiala en producción
    private final long EXPIRACION = 1000 * 60 * 60 * 10; // 10 horas

    public String generarToken(UUID usuarioId, String email, String rol) {
        return Jwts.builder()
                .setSubject(usuarioId.toString())
                .claim("email", email)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRACION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extraerEmail(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("email", String.class);
    }

    public UUID extraerUsuarioId(String token) {
        String id = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return UUID.fromString(id);
    }

    public String extraerRol(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("rol", String.class);
    }
}
