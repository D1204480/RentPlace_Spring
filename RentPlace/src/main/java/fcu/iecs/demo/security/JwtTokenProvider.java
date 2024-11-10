// src/main/java/com/your/package/security/JwtTokenProvider.java
package fcu.iecs.demo.security;

import fcu.iecs.demo.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

  @Value("${app.jwt.secret}")
  private String jwtSecret;

  @Value("${app.jwt.expiration}")
  private int jwtExpirationMs;

  public String generateToken(User user) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

    return Jwts.builder()
        .setSubject(user.getUserId())  // 使用 String 類型的 userId
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .claim("email", user.getEmail())
        .claim("name", user.getUsername())
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public String getUserIdFromJWT(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(token)
        .getBody();

    return claims.getSubject();
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parser()
          .setSigningKey(jwtSecret)
          .parseClaimsJws(authToken);
      return true;
    } catch (SignatureException ex) {
      System.out.println("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      System.out.println("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      System.out.println("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      System.out.println("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      System.out.println("JWT claims string is empty.");
    }
    return false;
  }
}