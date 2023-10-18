package allercheck.backend.global.jwt;

import allercheck.backend.domain.auth.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@PropertySource("classpath:secure.properties")
public class TokenProvider {

    private Key key;
    private long validityTime;

    public TokenProvider(@Value("${jwt.secret-key}") String secretKey,
                         @Value("${jwt.token.expire-length}") long validityTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.validityTime = validityTime;
    }

    public String createToken(final String payload) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + validityTime);

        return Jwts.builder()
                .setSubject(payload)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getPayLoad(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public void validateToken(final String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().
                    setSigningKey(key).
                    build().
                    parseClaimsJws(token);
        } catch(JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException();
        }
    }
}
