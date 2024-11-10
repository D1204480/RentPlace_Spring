// src/main/java/com/your/package/security/JwtTokenProvider.java
package fcu.iecs.demo.security;

import fcu.iecs.demo.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@Component
//public class JwtTokenProvider {
//
//  @Value("${jwt.secret}")
//  private String jwtSecret;
//
//  @Value("${jwt.expiration}")
//  private int jwtExpirationMs;
//
//  // 生成 JWT Token
//  public String generateToken(User user) {
//    Date now = new Date();
//    Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
//
//    return Jwts.builder()
//        .setSubject(user.getUserId())  // 使用 String 類型的 userId
//        .setIssuedAt(now)
//        .setExpiration(expiryDate)
//        .claim("email", user.getEmail())
//        .claim("name", user.getUsername())
//        .signWith(SignatureAlgorithm.HS512, jwtSecret)
//        .compact();
//  }
//
//  public String getUserIdFromJWT(String token) {
//    Claims claims = Jwts.parser()
//        .setSigningKey(jwtSecret)
//        .parseClaimsJws(token)
//        .getBody();
//
//    return claims.getSubject();
//  }
//
//  // 從 JWT Token 中獲取用戶名
//  public String getUsernameFromJWT(String token) {
//    Claims claims = Jwts.parser()
//        .setSigningKey(jwtSecret)
//        .parseClaimsJws(token)
//        .getBody();
//
//    return claims.getSubject();
//  }
//
//  // 驗證 JWT Token
//  public boolean validateToken(String authToken) {
//    try {
//      Jwts.parser()
//          .setSigningKey(jwtSecret)
//          .parseClaimsJws(authToken);
//      return true;
//    } catch (SignatureException ex) {
//      System.out.println("Invalid JWT signature");
//    } catch (MalformedJwtException ex) {
//      System.out.println("Invalid JWT token");
//    } catch (ExpiredJwtException ex) {
//      System.out.println("Expired JWT token");
//    } catch (UnsupportedJwtException ex) {
//      System.out.println("Unsupported JWT token");
//    } catch (IllegalArgumentException ex) {
//      System.out.println("JWT claims string is empty.");
//    }
//    return false;
//  }
//}

@Component
@Slf4j
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private int jwtExpirationMs;

  // 生成 JWT Token
  public String generateToken(User user) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

    return Jwts.builder()
        .setSubject(user.getUserId())
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .claim("email", user.getEmail())
        .claim("name", user.getUsername())
        .signWith(SignatureAlgorithm.HS512, jwtSecret)  // 修改這裡
        .compact();
  }

  // 從 JWT Token 中獲取用戶 ID
  public String getUserIdFromJWT(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(jwtSecret)  // 修改這裡
        .parseClaimsJws(token)
        .getBody();

    return claims.getSubject();
  }

  // 從 JWT Token 中獲取用戶名
  public String getUsernameFromJWT(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(jwtSecret)  // 修改這裡
        .parseClaimsJws(token)
        .getBody();

    return claims.get("name", String.class);
  }

  // 驗證 JWT Token
  public boolean validateToken(String authToken) {
    try {
      Jwts.parser()
          .setSigningKey(jwtSecret)  // 修改這裡
          .parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      log.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }

  // 從 token 中獲取所有用戶信息
  public Map<String, Object> getUserDetailsFromJWT(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(jwtSecret)  // 修改這裡
        .parseClaimsJws(token)
        .getBody();

    Map<String, Object> details = new HashMap<>();
    details.put("userId", claims.getSubject());
    details.put("email", claims.get("email", String.class));
    details.put("name", claims.get("name", String.class));

    return details;
  }
}