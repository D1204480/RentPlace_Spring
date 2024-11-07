package fcu.iecs.demo.service;

import fcu.iecs.demo.dto.AuthenticationResponse;
import fcu.iecs.demo.dto.LoginRequest;
import fcu.iecs.demo.dto.RegisterRequest;
import fcu.iecs.demo.model.User;
import fcu.iecs.demo.model.VerificationToken;
import fcu.iecs.demo.repository.UserRepository;
import fcu.iecs.demo.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public AuthenticationResponse registerUser(RegisterRequest request) {
        // Check if user exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setEnabled(false);

        User savedUser = userRepository.save(user);

        // Generate verification token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(
                token,
                savedUser,
                LocalDateTime.now().plusHours(24)
        );
        tokenRepository.save(verificationToken);

        // Create verification link
        String verificationLink = "http://localhost:8080/api/auth/verify?token=" + token;

        return new AuthenticationResponse(
                "註冊成功！請檢查您的郵箱進行驗證。",
                verificationLink,
                token,
                true
        );
    }

    public AuthenticationResponse login(LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .filter(User::isEnabled)
                .map(user -> new AuthenticationResponse(
                        "登入成功！",
                        null,
                        generateJwtToken(user), // Implement JWT token generation
                        true
                ))
                .orElseThrow(() -> new RuntimeException("Invalid credentials or account not verified"));
    }

    public void verifyAccount(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.delete(verificationToken);
    }

    private String generateJwtToken(User user) {
        // Implement JWT token generation logic
        return "jwt-token";
    }
}