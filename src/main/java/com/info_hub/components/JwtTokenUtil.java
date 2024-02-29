package com.info_hub.components;

import com.info_hub.dtos.auth.TokenDTO;
import com.info_hub.exceptions.TokenException;
import com.info_hub.models.Token;
import com.info_hub.repositories.TokenRepository;
import com.info_hub.services.auth.TokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    private final TokenService tokenService;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.exp-access-token}")
    private long expirationAccessToken;

    @Value("${jwt.exp-refresh-token}")
    private long expirationRefreshToken;
    private final TokenRepository tokenRepository;

    // generate token
    public TokenDTO generateToken(com.info_hub.models.User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());

        LocalDateTime accessTokenExp = LocalDateTime.now().plusSeconds(expirationAccessToken);
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                // parse LocalDateTime to Date.
                .setExpiration(Date.from(accessTokenExp.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact(); // build

        LocalDateTime refreshTokenExp = LocalDateTime.now().plusSeconds(expirationRefreshToken);
        String refreshToken = UUID.randomUUID().toString();

        return TokenDTO.builder()
                .accessToken(accessToken)
                .expirationAccessToken(accessTokenExp)
                .refreshToken(refreshToken)
                .expirationRefreshToken(refreshTokenExp)
                .build();
    }

    private Key getSignKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    // extract claims from token
    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token) // token hết hạn nó sẽ chết ở đây.
                    .getBody();
        } catch (ExpiredJwtException expiredJwtException) {
            throw new TokenException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        }
    }

    // extract claim from claims
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // get username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // check expiration
    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    /**
     * Validate token
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            Token existingToken = tokenRepository.findByAccessToken(token);
            if (existingToken == null || existingToken.isRevoked() || !userDetails.isEnabled()) {
                return false;
            }
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (UnsupportedJwtException unsupportedJwtException) {
            throw new TokenException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new TokenException(HttpStatus.BAD_REQUEST, "Jwt claims string is null or empty");
        } catch (MalformedJwtException malformedJwtException) {
            throw new TokenException(HttpStatus.BAD_REQUEST, "Invalid JWT Token");
        }

    }

    // generate reset password token when forgot password
    public String generateResetToken() {
        return UUID.randomUUID().toString();
    }
}
