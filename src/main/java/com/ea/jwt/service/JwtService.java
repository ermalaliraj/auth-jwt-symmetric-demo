package com.ea.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final String privateKey = "SECRET_KEY";
    private byte[] privateKeyBytes = DatatypeConverter.parseBase64Binary(privateKey);
    private Key signingKeyPrivate = new SecretKeySpec(privateKeyBytes, signatureAlgorithm.getJcaName());

    public String generateJwtTokenSymmetric() {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + 60 * 1000;
        Date exp = new Date(expMillis);

        JwtBuilder jwtBuilder = Jwts.builder().setId("id")
                .setIssuedAt(now)
                .setExpiration(exp)
                .setSubject("JWT issued by Server to test Symmetric key. It will be verified with the same key")
                .setIssuer("server")
                .signWith(signatureAlgorithm, signingKeyPrivate);
        if (exp != null) {
            jwtBuilder.setExpiration(exp);
        }
        return jwtBuilder.compact();
    }

    public Jwt verifyWithSymmetricKey(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(privateKey))
                .parseClaimsJws(token).getBody();

        System.out.println("claims" + claims);
        Map<String, Object> headers = new HashMap<>();
        headers.put("1", "header");
        Jwt jwt = new Jwt(claims.getId(), getInstant(claims.getIssuedAt()), getInstant(claims.getExpiration()), headers, claims);
        return jwt;
    }

    private Instant getInstant(Date date) {
        if (date != null) {
            return date.toInstant();
        }
        return null;
    }
}
