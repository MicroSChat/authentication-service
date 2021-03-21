package com.microschat.authenticationservice.authentication;

import com.microschat.authenticationservice.common.UserInformationRepository;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TokenService {

    private final SecurityConfigurationProperties securityConfigurationProperties;

    private final String secretKeyEncoded;

    @Autowired
    public TokenService(SecurityConfigurationProperties securityConfigurationProperties) {
        this.securityConfigurationProperties = securityConfigurationProperties;

        secretKeyEncoded = Base64.getEncoder().encodeToString(securityConfigurationProperties.getSecretKey().getBytes());
    }


    public String createToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + securityConfigurationProperties.getTokenExpireLength());
        log.info("Issuing token using signing key {}", secretKeyEncoded);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKeyEncoded)
                .compact();
    }

    public boolean validateToken(String token) throws AuthenticationException {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKeyEncoded).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthenticationException(String.format("Expired or invalid JWT token %s", token));
        }
    }
}
