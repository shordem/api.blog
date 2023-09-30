package com.shordem.blog.utils;

import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.shordem.blog.service.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils extends SecurityProperties.Filter {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${shordem.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${shordem.app.jwtSecret}")
    private String jwtSecret;

    public String getSubjectFromJwtToken(String token) {
        if (token.startsWith("Bearer")) {
            token = token.substring(7);
        }

        SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

        String sub = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody().getSubject();
        return sub;
    }

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl adminPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

        String token = Jwts.builder()
                .setSubject(adminPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(secret, SignatureAlgorithm.HS256).compact();
        return token;
    }

    public boolean validateJwtToken(String token) {
        try {
            if (token.startsWith("Bearer")) {
                token = token.substring(7);
            }

            SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

}
