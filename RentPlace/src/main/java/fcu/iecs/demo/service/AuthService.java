package fcu.iecs.demo.service;

import fcu.iecs.demo.dto.request.GoogleLoginRequest;
import fcu.iecs.demo.dto.request.LoginRequest;
import fcu.iecs.demo.dto.request.RegisterRequest;
import fcu.iecs.demo.dto.response.AuthResponse;
import fcu.iecs.demo.dto.response.JwtAuthenticationResponse;
import fcu.iecs.demo.dto.response.UserDTO;
import fcu.iecs.demo.model.User;
import fcu.iecs.demo.repository.UserRepository;
import fcu.iecs.demo.security.JwtTokenProvider;
import fcu.iecs.demo.util.UserIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
@Transactional
public class AuthService {
  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserIdGenerator userIdGenerator;

//  @Autowired
//  private UserRepository userRepository;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

//  @Autowired
//  private UserIdGenerator userIdGenerator;  // 注入 UserIdGenerator

  public AuthService(UserRepository userRepository,
                     PasswordEncoder passwordEncoder,
                     UserIdGenerator userIdGenerator) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
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

  public JwtAuthenticationResponse login(LoginRequest loginRequest) {
    // 驗證用戶
    User user = userRepository.findByUsername(loginRequest.getUsername())
        .orElseThrow(() -> new RuntimeException("User not found"));

    // 檢查密碼
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      throw new RuntimeException("Invalid password");
    }

    // 生成 Token
    String jwt = generateToken(user);

    return new JwtAuthenticationResponse(jwt);
  }

  private String generateToken(User user) {
    // 這裡需要實現 JWT token 生成邏輯
    // 可以使用之前創建的 JwtTokenProvider
    return "generated-token";  // 臨時返回值，需要替換為實際的 token
  }
}
