package fcu.iecs.demo.controller;

import fcu.iecs.demo.model.EmailRequest;
import fcu.iecs.demo.model.VerificationRequest;
import fcu.iecs.demo.model.VerificationResponse;
import fcu.iecs.demo.repository.VerificationCodeRepository;
import fcu.iecs.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api")
public class RegistrationController {

  @Autowired
  private EmailService emailService;

  @Autowired
  private VerificationCodeRepository verificationCodeRepository;

  @PostMapping("/send-verification-code")
  public ResponseEntity<String> sendVerificationCode(@RequestBody EmailRequest emailRequest) {
    String verificationCode = emailService.sendVerificationCode(emailRequest.getEmail());
    return ResponseEntity.ok(verificationCode);
  }

  @PostMapping("/verify-code")
  public ResponseEntity<VerificationResponse> verifyCode(@RequestBody VerificationRequest verificationRequest) {
    boolean isValid = verificationCodeRepository.isCodeValid(verificationRequest.getEmail(), verificationRequest.getCode());
    return ResponseEntity.ok(new VerificationResponse(isValid));
  }
}