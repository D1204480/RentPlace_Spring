// src/main/java/com/your/package/security/JwtTokenProvider.java
package fcu.iecs.demo.security;

import fcu.iecs.demo.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

//@Component
//@Slf4j
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
//        .setSubject(user.getUserId())
//        .setIssuedAt(now)
//        .setExpiration(expiryDate)
//        .claim("email", user.getEmail())
//        .claim("name", user.getUsername())
//        .signWith(SignatureAlgorithm.HS512, jwtSecret)  // 修改這裡
//        .compact();
//  }
//
//  // 從 JWT Token 中獲取用戶 ID
//  public String getUserIdFromJWT(String token) {
//    Claims claims = Jwts.parser()
//        .setSigningKey(jwtSecret)  // 修改這裡
//        .parseClaimsJws(token)
//        .getBody();
//
//    return claims.getSubject();
//  }
//
//  // 從 JWT Token 中獲取用戶名
//  public String getUsernameFromJWT(String token) {
//    Claims claims = Jwts.parser()
//        .setSigningKey(jwtSecret)  // 修改這裡
//        .parseClaimsJws(token)
//        .getBody();
//
//    return claims.get("name", String.class);
//  }
//
//  // 驗證 JWT Token
//  public boolean validateToken(String authToken) {
//    try {
//      Jwts.parser()
//          .setSigningKey(jwtSecret)  // 修改這裡
//          .parseClaimsJws(authToken);
//      return true;
//    } catch (SignatureException e) {
//      log.error("Invalid JWT signature: {}", e.getMessage());
//    } catch (MalformedJwtException e) {
//      log.error("Invalid JWT token: {}", e.getMessage());
//    } catch (ExpiredJwtException e) {
//      log.error("JWT token is expired: {}", e.getMessage());
//    } catch (UnsupportedJwtException e) {
//      log.error("JWT token is unsupported: {}", e.getMessage());
//    } catch (IllegalArgumentException e) {
//      log.error("JWT claims string is empty: {}", e.getMessage());
//    }
//    return false;
//  }
//
//  // 從 token 中獲取所有用戶信息
//  public Map<String, Object> getUserDetailsFromJWT(String token) {
//    Claims claims = Jwts.parser()
//        .setSigningKey(jwtSecret)  // 修改這裡
//        .parseClaimsJws(token)
//        .getBody();
//
//    Map<String, Object> details = new HashMap<>();
//    details.put("userId", claims.getSubject());
//    details.put("email", claims.get("email", String.class));
//    details.put("name", claims.get("name", String.class));
//
//    return details;
//  }
//}

@Component
@Slf4j
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private int jwtExpirationMs;

  private static final String TOKEN_TYPE = "Bearer";
  private static final String TOKEN_ISSUER = "RentPlace";
  private static final String AUTHORITIES_KEY = "roles";

  @PostConstruct
  protected void init() {
    // Base64 encode the secret key for better security
    jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
  }

//  // 生成 JWT Token
//  public String generateToken(User user, Collection<? extends GrantedAuthority> authorities) {
//    byte[] signingKey = jwtSecret.getBytes();
//
//    Map<String, Object> claims = new HashMap<>();
//    claims.put("email", user.getEmail());
//    claims.put("name", user.getUsername());
//    claims.put(AUTHORITIES_KEY, authorities.stream()
//        .map(GrantedAuthority::getAuthority)
//        .collect(Collectors.toList()));
//
//    Date now = new Date();
//    Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
//
//    try {
//      return Jwts.builder()
//          .setClaims(claims)
//          .setSubject(user.getUserId())
//          .setIssuer(TOKEN_ISSUER)
//          .setIssuedAt(now)
//          .setExpiration(expiryDate)
//          .signWith(SignatureAlgorithm.HS512, signingKey)
//          .compact();
//    } catch (Exception e) {
//      log.error("Error generating JWT token", e);
//      throw new RuntimeException("Could not generate token", e);
//    }
//  }

//   生成 JWT Token
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
    try {
      return getClaimsFromToken(token).getSubject();
    } catch (Exception e) {
      log.error("Error getting user ID from token", e);
      throw new RuntimeException("Could not get user ID from token", e);
    }
  }

  // 從 JWT Token 中獲取用戶名
  public String getUsernameFromJWT(String token) {
    try {
      return getClaimsFromToken(token).get("name", String.class);
    } catch (Exception e) {
      log.error("Error getting username from token", e);
      throw new RuntimeException("Could not get username from token", e);
    }
  }

  // 驗證 JWT Token
  public boolean validateToken(String authToken) {
    try {
      Claims claims = getClaimsFromToken(authToken);

      // 檢查 token 是否過期
      if (claims.getExpiration().before(new Date())) {
        log.warn("Token is expired");
        return false;
      }

      // 檢查發行者
      if (!TOKEN_ISSUER.equals(claims.getIssuer())) {
        log.warn("Invalid token issuer");
        return false;
      }

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
    try {
      Claims claims = getClaimsFromToken(token);

      Map<String, Object> details = new HashMap<>();
      details.put("userId", claims.getSubject());
      details.put("email", claims.get("email", String.class));
      details.put("name", claims.get("name", String.class));
      details.put("roles", claims.get(AUTHORITIES_KEY));
      details.put("issuedAt", claims.getIssuedAt());
      details.put("expiresAt", claims.getExpiration());

      return details;
    } catch (Exception e) {
      log.error("Error getting user details from token", e);
      throw new RuntimeException("Could not get user details from token", e);
    }
  }

  // 解析 token 獲取 Claims
  private Claims getClaimsFromToken(String token) {
    return Jwts.parser()
        .setSigningKey(jwtSecret.getBytes())
        .parseClaimsJws(token)
        .getBody();
  }

  // 從請求頭中提取 token
  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_TYPE + " ")) {
      return bearerToken.substring(TOKEN_TYPE.length() + 1);
    }
    return null;
  }

  // 檢查 token 是否即將過期
  public boolean isTokenAboutToExpire(String token) {
    try {
      Claims claims = getClaimsFromToken(token);
      Date expirationDate = claims.getExpiration();
      Date now = new Date();

      // 檢查是否在最後24小時內
      long timeUntilExpiration = expirationDate.getTime() - now.getTime();
      return timeUntilExpiration < 86400000; // 24 hours in milliseconds
    } catch (Exception e) {
      return false;
    }
  }
}