package fcu.iecs.demo.service;

import fcu.iecs.demo.dto.request.GoogleLoginRequest;
import fcu.iecs.demo.dto.response.AuthResponse;
import fcu.iecs.demo.dto.response.UserDTO;
import fcu.iecs.demo.model.User;
import fcu.iecs.demo.repository.UserRepository;
import fcu.iecs.demo.security.JwtTokenProvider;
import fcu.iecs.demo.util.UserIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AuthService {
  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private UserIdGenerator userIdGenerator;  // 注入 UserIdGenerator

  @Transactional
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
}
