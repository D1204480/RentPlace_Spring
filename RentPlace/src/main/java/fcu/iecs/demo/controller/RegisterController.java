package fcu.iecs.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import fcu.iecs.demo.model.User;
import fcu.iecs.demo.model.VerificationToken;
import fcu.iecs.demo.repository.UserRepository;
import fcu.iecs.demo.repository.VerificationTokenRepository;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // 保存用戶
        user.setEnabled(false); // 確保新用戶默認是未啟用的
        User savedUser = userRepository.save(user);

        // 生成驗證token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(
                token,
                savedUser,
                LocalDateTime.now().plusHours(24)
        );
        tokenRepository.save(verificationToken);

        // 返回驗證連結（在實際應用中，這應該通過郵件發送）
        String verificationLink = "http://localhost:8080/api/verify?token=" + token;

        return ResponseEntity.ok()
                .body(Map.of(
                        "message", "用戶註冊成功，請檢查驗證連結",
                        "verificationLink", verificationLink,
                        "token", token
                ));
    }
}
