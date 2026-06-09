package com.skillbox.socialnetwork.api.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * JWT провайдер.
 */
@Component
public class JwtProvider {

    private static final Integer TOKEN_DAYS_EXPIRED = 15;
    private static final String JWT_SECRET = "jwtSecret"; // пока так

    /**
     * Генерация токена.
     * @param login
     * @return
     */
    public String generateToken(String login) {
        Date date = Date.from(LocalDate.now().plusDays(TOKEN_DAYS_EXPIRED).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    /**
     * Валидация токена.
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException expEx) {

            return false;
        }
    }

    /**
     * Получение логина из токена.
     * @param token
     * @return
     */
    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}
