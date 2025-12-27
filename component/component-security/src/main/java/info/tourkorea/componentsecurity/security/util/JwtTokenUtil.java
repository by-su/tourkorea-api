package info.tourkorea.componentsecurity.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenUtil implements InitializingBean {

    @Value("${jwt.accessToken-expired-time-ms}")
    private Long ACCESS_TOKEN_EXPIRED_TIME_MS;
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    private Key key;

    private static final String USER_ID = "user_id";
    private static final String USERNAME = "username";
    private static final String PROFILE_IMAGE = "profile_image";

    @Override
    public void afterPropertiesSet() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Map<String, String> infos) {

        Claims claims = Jwts.claims();
        claims.putAll(infos);

        var now = (new Date()).getTime();
        var validity = new Date(now + ACCESS_TOKEN_EXPIRED_TIME_MS);

        return Jwts.builder()
                .setSubject(USER_ID)
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public Long getUserId(String token) {

        System.out.println(token);
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.get(USER_ID).toString());
    }

    public String getUsername(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get(USERNAME).toString();
    }

    public String getProfileImage(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return (String) claims.get(PROFILE_IMAGE);
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new JwtException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new JwtException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new JwtException("JWT 토큰이 잘못되었습니다.");
        }
    }
}
