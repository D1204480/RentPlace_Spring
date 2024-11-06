package fcu.iecs.demo.service;

import fcu.iecs.demo.model.User;
import fcu.iecs.demo.model.VerificationToken;
import fcu.iecs.demo.repository.UserRepository;
import fcu.iecs.demo.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserTokenService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void registerUser(User user) {
        userRepository.save(user);
        String token = UUID.randomUUID().toString();
        saveVerificationToken(user, token);
        sendVerificationEmail(user.getEmail(), token);
    }

    private void saveVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(token, user, LocalDateTime.now().plusDays(1));
        tokenRepository.save(verificationToken);
    }

    public void sendVerificationEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("請驗證您的帳號");
        message.setText("請點擊以下連結驗證您的帳號：\n" +
                "http://localhost:8080/api/auth/verify?token=" + token);

        mailSender.send(message);
    }
}
