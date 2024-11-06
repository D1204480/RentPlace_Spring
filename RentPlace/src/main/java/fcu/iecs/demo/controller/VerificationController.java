package fcu.iecs.demo.controller;

import fcu.iecs.demo.model.User;
import fcu.iecs.demo.model.VerificationToken;
import fcu.iecs.demo.repository.UserRepository;
import fcu.iecs.demo.repository.VerificationTokenRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class VerificationController {

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        Optional<VerificationToken> verificationTokenOpt = tokenRepository.findByToken(token);

        if (verificationTokenOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("無效的驗證連結！");
        }

        VerificationToken verificationToken = verificationTokenOpt.get();
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("驗證連結已過期！");
        }

        User user = verificationToken.getUser();
        user.setEnabled(true); // 將使用者設為已啟用
        userRepository.save(user);
        tokenRepository.delete(verificationToken); // 刪除 token

        return ResponseEntity.ok("帳戶已成功驗證！");
    }
}
