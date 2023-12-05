package com.example.ProjectBlog.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

@Service
public class TokenService {
    private static final String SECRET_KEY = "8f3ca84c5122366a19f7804e71c432fc639c7817903321a43879fe8c436c2311";
    private static final int expirationTimeMillis = 100000 * 60 * 24;

    public String extractUserId(String token){
        return extractClaim(token, Claims ::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateJwtToken(
            UserDetails user
    ){
        return Jwts
                .builder()
                .setHeaderParam("type","JWT")
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
                .signWith(getSigningKey(),SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails user){
        final String id = extractUserId(token);
        return Objects.equals(id, user.getUsername());
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token,Claims :: getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .base64UrlDecodeWith(Decoders.BASE64URL)
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
