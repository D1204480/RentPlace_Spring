// src/main/java/com/your/package/config/SecurityConfig.java
package fcu.iecs.demo.config;

import fcu.iecs.demo.repository.UserRepository;
import fcu.iecs.demo.security.JwtAuthenticationFilter;
import fcu.iecs.demo.security.JwtTokenProvider;
import fcu.iecs.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//
//  @Autowired
//  private final JwtAuthenticationFilter jwtAuthenticationFilter;
//
//  public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
//    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//  }
//
//  @Bean
//  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    http
//        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//        .csrf(csrf -> csrf.disable())
//        .authorizeHttpRequests(authorize -> authorize
//            .requestMatchers(
//                "/auth/**",
//                "/api/auth/**",
//                "/api/send-verification-code",
//                "/api/verify-code",
//                "/api/auth/**",
//                "/api/register",
//                "/api/login",
//                "/api/public/**"
//            ).permitAll()
//            .anyRequest().authenticated()
//        )
//        .sessionManagement(session -> session
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        )
//        // 添加 JWT 過濾器
//        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//    return http.build();
//  }
//
//
//  @Bean
//  public CorsConfigurationSource corsConfigurationSource() {
//    CorsConfiguration configuration = new CorsConfiguration();
//    configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
//    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//    configuration.setAllowedHeaders(Arrays.asList(
//        "Authorization",
//        "Content-Type",
//        "Accept",
//        "X-Requested-With",
//        "remember-me",
//        "Cache-Control",
//        "verify-code"
//    ));
//    configuration.setAllowCredentials(true);
//    configuration.setMaxAge(3600L);
//
//    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//    source.registerCorsConfiguration("/**", configuration);
//    return source;
//  }
//
//  @Bean
//  public JwtAuthenticationFilter jwtAuthenticationFilter() {
//    return new JwtAuthenticationFilter();
//  }
//
//  @Bean
//  public PasswordEncoder passwordEncoder() {
//    return new BCryptPasswordEncoder();
//  }
//
//  @Bean
//  public AuthenticationManager authenticationManager(
//      AuthenticationConfiguration authenticationConfiguration) throws Exception {
//    return authenticationConfiguration.getAuthenticationManager();
//  }
//
//  @Bean
//  public WebSecurityCustomizer webSecurityCustomizer() {
//    return (web) -> web.ignoring()
//        .requestMatchers(
//            "/v3/api-docs/**",
//            "/swagger-ui/**",
//            "/swagger-ui.html"
//        );
//  }
//
//}

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtTokenProvider tokenProvider;

  public SecurityConfig(JwtTokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(
                "/api/auth/**",
                "/api/send-verification-code",
                "/api/verify-code",
                "/api/register",
                "/api/login",
                "/api/public/**"
            ).permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(tokenProvider);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}