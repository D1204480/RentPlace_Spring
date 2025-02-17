// src/main/java/com/your/package/config/SecurityConfig.java
package fcu.iecs.demo.config;

import fcu.iecs.demo.security.CustomUserDetailsService;
import fcu.iecs.demo.security.JwtAuthenticationFilter;
import fcu.iecs.demo.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity  // 啟用 Spring Security
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtTokenProvider tokenProvider;
  private final CustomUserDetailsService customUserDetailsService;

  public SecurityConfig(JwtTokenProvider tokenProvider,
                        CustomUserDetailsService customUserDetailsService) {
    this.tokenProvider = tokenProvider;
    this.customUserDetailsService = customUserDetailsService;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))   //允許跨域請求配置
        .csrf(csrf -> csrf.disable())   //停用 CSRF 保護
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint((request, response, authException) -> {
              response.setContentType("application/json;charset=UTF-8");
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              response.getWriter().write("{\"message\":\"" + authException.getMessage() + "\"}");
            }))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(
                "/auth/**",
                "/api/auth/**",
                "/api/send-verification-code",
                "/api/verify-code",
                "/api/auth/**",
                "/api/user/**",
                "/api/login",
                "/api/public/**",
                "/api/venues/**",
                "/api/images/**",
                "/api/equipment/**",
                "/api/reservations/**",
                "/api/close-dates/**",
                "/api/payments/**",
                "api/orders/**",
                "/api/orders/qrcode/decode",
                "api/orders/latest-qrcode",
                "api/orders/qr-code/{encryptedContent}",
                "api/orders/qrcode/decode",
                "api/orders/qrcode/decrypt",
                "api/statistics/**"

            ).permitAll()
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 允許所有 OPTIONS 請求
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)   //設定為無狀態（STATELESS）
        )
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

// 方法1：使用具體的允許來源（推薦用於本地開發環境）
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:5174", "http://localhost:63342",
        "https://d1204480.github.io", "https://d1204416.github.io", "http://127.0.0.1:5500/"));
//    configuration.setAllowCredentials(true);  // 允許攜帶認證訊息, 當使用具體來源時，可以設為 true

    // 方法2：允許所有來源（僅用於測試）
    configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
    configuration.setAllowCredentials(false);  // 使用 setAllowedOriginPatterns 時必須設為 false

    // 允許的 HTTP 方法
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    // 允許的 HTTP 標頭
    configuration.setAllowedHeaders(Arrays.asList(
        "Authorization",
        "Content-Type",
        "Accept",
        "X-Requested-With",
        "remember-me",
        "Cache-Control",
        "verify-code"
    ));

    configuration.setMaxAge(3600L);   // 預檢請求的有效期

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(tokenProvider, customUserDetailsService);
  }

  @Bean
  // 密碼加密器
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  // 認證管理器配置
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  // 忽略 Swagger 文檔相關路徑的安全檢查
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
        .requestMatchers(
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
        );
  }
}

//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//
//  private final JwtTokenProvider tokenProvider;
//
//  public SecurityConfig(JwtTokenProvider tokenProvider) {
//    this.tokenProvider = tokenProvider;
//  }
//
//  @Bean
//  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    http
//        .cors(Customizer.withDefaults())
//        .csrf(csrf -> csrf.disable())
//        .authorizeHttpRequests(authorize -> authorize
//            .requestMatchers(
//                "/api/auth/**",
//                "/api/send-verification-code",
//                "/api/verify-code",
//                "/api/register",
//                "/api/login",
//                "/api/public/**"
//            ).permitAll()
//            .anyRequest().authenticated()
//        )
//        .sessionManagement(session -> session
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        )
//        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//    return http.build();
//  }
//
//  @Bean
//  public JwtAuthenticationFilter jwtAuthenticationFilter() {
//    return new JwtAuthenticationFilter(tokenProvider);
//  }
//
//  @Bean
//  public PasswordEncoder passwordEncoder() {
//    return new BCryptPasswordEncoder();
//  }
//
//  @Bean
//  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//    return authConfig.getAuthenticationManager();
//  }
//
//  @Bean
//  public CorsConfigurationSource corsConfigurationSource() {
//    CorsConfiguration configuration = new CorsConfiguration();
//    configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
//    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//    configuration.setAllowedHeaders(Arrays.asList("*"));
//    configuration.setAllowCredentials(true);
//
//    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//    source.registerCorsConfiguration("/**", configuration);
//    return source;
//  }
//}