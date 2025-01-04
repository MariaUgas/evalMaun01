package com.example.maun.service;

import com.example.maun.entity.User;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {


    @Value("${token.jwt.expiration-minutes}")
    private long EXPIRATION_MINUTES;

    @Value("${token.jwt.secret-key}")
    private String SECRET_KEY;

    @Autowired
    SimulatedRedisService redis;

    public String generateToken(User user, Map<String, Object> extraClaims) {

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date( issuedAt.getTime() + (EXPIRATION_MINUTES * 60 * 1000) );

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getName())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public void saveToken(String key, String token) {
        redis.saveToken(key, token,  (EXPIRATION_MINUTES * 60 * 1000));
    }

    public String getToken(String key) {
        return redis.getToken(key);
    }

    private Key generateKey() {
        byte[] secretAsBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(secretAsBytes);
    }
}
