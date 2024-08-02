package com.api.conversation.conversation_api.config.webSecurity.jwt;

import com.api.conversation.conversation_api.models.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JWTService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.time-expiration}")
    private Long timeExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> ClaimsResolver){
        final Claims claims = extractAllClaims(jwtToken);
        return ClaimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String jwtToken){
        return Jwts.parserBuilder()
                .setSigningKey(getSignIngKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Key getSignIngKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //Gerando o Token
    public String generateToken(UserModel userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        claims.put("username", userDetails.getName());
        claims.put("nickname", userDetails.getNickname());
        return generateToken(claims, userDetails);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails){
        System.out.println(claims);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + timeExpiration))
                .signWith(getSignIngKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //Verificando se o token e valido
    public boolean isValidToken(String jwtToken, UserDetails userDetails){
        final String username = extractUsername(jwtToken);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken));
    }

    public boolean isTokenExpired(String jwtToken){
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken,Claims::getExpiration);
    }
}
