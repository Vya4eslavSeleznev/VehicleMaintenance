package com.vehicle.maintenance.security.jwt;

import com.vehicle.maintenance.exception.InvalidJwtAuthenticationException;
import com.vehicle.maintenance.model.Role;
import com.vehicle.maintenance.model.UserPrincipalModel;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private @Value("${secretKey}") String secretKey;
    private @Value("${validityInMs}") String validityInMs;

    private String getUserName(String token) {
        return Jwts
          .parser()
          .setSigningKey(secretKey)
          .parseClaimsJws(token)
          .getBody()
          .getSubject();
    }

    private String getUserRole(String token) {
        return Jwts
          .parser()
          .setSigningKey(secretKey)
          .parseClaimsJws(token)
          .getBody()
          .get("role")
          .toString();
    }

    public String createToken(String userName, Role role) {
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put("role", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + Long.parseLong(validityInMs));

        return Jwts
          .builder()
          .setClaims(claims)
          .setIssuedAt(now)
          .setExpiration(validity)
          .signWith(SignatureAlgorithm.HS256, secretKey)
          .compact();
    }

    public boolean validateToken(String token) throws InvalidJwtAuthenticationException {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        }
        catch(JwtException | IllegalArgumentException e) {
            throw new InvalidJwtAuthenticationException();
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public Authentication getAuthentication(String token) {
        UserPrincipalModel userPrincipalModel = new UserPrincipalModel(getUserName(token), List.of(getUserRole(token)));

        return new UsernamePasswordAuthenticationToken(userPrincipalModel, "", userPrincipalModel.getAuthorities());
    }

    @PostConstruct
    private void init() {
        secretKey = Base64
          .getEncoder()
          .encodeToString(secretKey.getBytes());
    }
}
