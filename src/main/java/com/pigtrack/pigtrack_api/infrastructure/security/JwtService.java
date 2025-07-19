package com.pigtrack.pigtrack_api.infrastructure.security;

import com.pigtrack.pigtrack_api.domain.models.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // Llave secreta para firmar los tokens (puedes moverla a application.properties si quieres)
    private static final String SECRET_KEY = "q4sZh3Vb5Hn2J0rL98pQ4xMvW7z/T1L6P+9YxWk+g2zq9J7j8iK9qA3blU+7YpF5";

    // Duración del token: 1 día
    private static final long EXPIRATION_TIME_MS = 1000 * 60 * 60 * 24;

    private Key obtenerClave() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generarToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getId().toString())  // Usamos el ID del usuario como subject
                .claim("email", usuario.getEmail())      // Puedes agregar más claims si quieres
                .claim("rol", usuario.getRol())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(obtenerClave(), SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(obtenerClave())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extraerSubject(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(obtenerClave())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extraerClaim(String token, String claimName) {
        return Jwts
                .parserBuilder()
                .setSigningKey(obtenerClave())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(claimName, String.class);
    }

    public String extraerEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(obtenerClave())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email", String.class);
    }




}
