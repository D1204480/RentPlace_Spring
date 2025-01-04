package fcu.iecs.demo.service;

import fcu.iecs.demo.dto.request.GoogleLoginRequest;
import fcu.iecs.demo.dto.request.LoginRequest;
import fcu.iecs.demo.dto.response.AuthResponse;
import fcu.iecs.demo.dto.response.JwtAuthenticationResponse;
import fcu.iecs.demo.dto.UserDTO;
import fcu.iecs.demo.model.User;
import fcu.iecs.demo.repository.UserRepository;
import fcu.iecs.demo.security.JwtTokenProvider;
import fcu.iecs.demo.util.UserIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AuthService {
  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserIdGenerator userIdGenerator;


  public AuthService(
      AuthenticationManager authenticationManager,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtTokenProvider jwtTokenProvider,
      UserIdGenerator userIdGenerator) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenProvider = jwtTokenProvider;
    this.userIdGenerator = userIdGenerator;
  }


  public AuthResponse handleGoogleLogin(GoogleLoginRequest request) {
    try {
      // 先檢查是否存在
      User user = userRepository.findByEmail(request.getEmail())
          .orElseGet(() -> {
            // 不存在則創建新用戶
            User newUser = new User();
            newUser.setUserId(userIdGenerator.generateNewUserId());
            newUser.setEmail(request.getEmail());
            newUser.setUsername(request.getName());
            newUser.setProvider("GOOGLE");
            // 設置其他欄位的預設值
            newUser.setPassword(null);  // 或給一個預設值
            newUser.setPhone(null);
            newUser.setGender(null);
            newUser.setBirth(null);
            return userRepository.save(newUser);
          });

      // 生成 JWT，使用已存在的用戶或新創建的用戶
      String jwt = jwtTokenProvider.generateToken(user);

      return new AuthResponse(jwt, UserDTO.fromEntity(user));

    } catch (Exception e) {
      // 添加日誌
      logger.error("Google login failed", e);
      throw new RuntimeException("Login failed");
    }
  }

  public Authentication authenticate(String username, String password) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password)
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);
      return authentication;

    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("帳號或密碼錯誤");
    } catch (Exception e) {
      throw new RuntimeException("認證過程發生錯誤", e);
    }
  }


//  public User registerUser(RegisterRequest registerRequest) {
//    // 檢查用戶名是否已存在
//    if (userRepository.existsByUsername(registerRequest.getUsername())) {
//      throw new RuntimeException("Username is already taken!");
//    }
//
//    // 檢查電子郵件是否已存在
//    if (userRepository.existsByEmail(registerRequest.getEmail())) {
//      throw new RuntimeException("Email is already in use!");
//    }
//
//    // 創建新用戶
//    User user = new User();
//    user.setUserId(userIdGenerator.generateNewUserId());  // 生成用戶ID
//    user.setUsername(registerRequest.getUsername());
//    user.setEmail(registerRequest.getEmail());
//    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
//    // 設置其他必要的字段
////    user.setCreatedAt(new Date());
////    user.setActive(true);
//
//    // 保存用戶
//    return userRepository.save(user);
//  }

//  public JwtAuthenticationResponse login(LoginRequest loginRequest) {
//    try {
//      // 使用 AuthenticationManager 進行認證
//      Authentication authentication = authenticationManager.authenticate(
//          new UsernamePasswordAuthenticationToken(
//              loginRequest.getUsername(),
//              loginRequest.getPassword()
//          )
//      );
//
//      // 設置 SecurityContext
//      SecurityContextHolder.getContext().setAuthentication(authentication);
//
//      // 獲取用戶信息
//      User user = userRepository.findByUsername(loginRequest.getUsername())
//          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//      // 獲取用戶權限
//      List<String> roles = authentication.getAuthorities().stream()
//          .map(GrantedAuthority::getAuthority)
//          .collect(Collectors.toList());
//
//      // 生成 Token
//      String jwt = jwtTokenProvider.generateToken(user);
//
//      return new JwtAuthenticationResponse(jwt);
//
//    } catch (BadCredentialsException e) {
//      log.error("Invalid username or password for user: {}", loginRequest.getUsername());
//      throw new BadCredentialsException("帳號或密碼錯誤");
//    } catch (UsernameNotFoundException e) {
//      log.error("User not found: {}", loginRequest.getUsername());
//      throw new UsernameNotFoundException("用戶不存在");
//    } catch (Exception e) {
//      log.error("Login error for user: {}", loginRequest.getUsername(), e);
//      throw new RuntimeException("登入過程發生錯誤");
//    }
//  }

  public JwtAuthenticationResponse login(LoginRequest loginRequest) {
    logger.info("Attempting login for email: {}", loginRequest.getEmail());

    try {
      // 1. 使用 email 查找用戶
      User user = userRepository.findByEmail(loginRequest.getEmail())
          .orElseThrow(() -> new UsernameNotFoundException("此 Email 尚未註冊"));

      // 2. 檢查用戶狀態 - 處理 null 的情況
      Integer statusId = user.getStatusId();
      if (statusId != null && statusId == 13) {
        logger.warn("User account not found: {}", loginRequest.getEmail());
        throw new UsernameNotFoundException("查無使用者");
      }

      logger.debug("Found user with email: {}", user.getEmail());

      // 3. 檢查密碼
      if (user.getPassword() == null) {
        throw new BadCredentialsException("此帳號使用第三方登入，請使用相應的登入方式");
      }

      if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
        throw new BadCredentialsException("Email 或密碼錯誤");
      }

      // 4. 生成 JWT
      String jwt = jwtTokenProvider.generateToken(user);

      logger.info("Login successful for email: {}", loginRequest.getEmail());

      // 5. 返回響應
      return new JwtAuthenticationResponse(
          jwt,
          "Bearer",
          user.getUserId(),
          user.getUsername(),
          user.getEmail(),
          user.getPhone()
      );

    } catch (UsernameNotFoundException e) {
      logger.error("User not found with email: {}", loginRequest.getEmail());
      throw new UsernameNotFoundException("此 Email 尚未註冊");
    } catch (BadCredentialsException e) {
      logger.error("Invalid password for email: {}", loginRequest.getEmail());
      throw new BadCredentialsException("Email 或密碼錯誤");
    } catch (Exception e) {
      logger.error("Login error for email: {}", loginRequest.getEmail(), e);
      throw new RuntimeException("登入過程發生錯誤");
    }
  }


  public JwtAuthenticationResponse generateToken(User user) {
    try {
      // 設置默認權限
      List<GrantedAuthority> authorities = Collections.singletonList(
          new SimpleGrantedAuthority("ROLE_USER")
      );

      // 生成 Token
      String jwt = jwtTokenProvider.generateToken(user);

      return new JwtAuthenticationResponse(
          jwt,
          "Bearer",
          user.getUserId(),
          user.getUsername(),
          user.getEmail(),
          user.getPhone());

    } catch (Exception e) {
      logger.error("Error generating token for user: {}", user.getUsername(), e);
      throw new RuntimeException("Token 生成失敗");
    }
  }
}
