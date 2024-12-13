package fcu.iecs.demo.service;

import fcu.iecs.demo.repository.VerificationCodeRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

  private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  private VerificationCodeRepository verificationCodeRepository;

  public String sendVerificationCode(String email) {
    try {
      String verificationCode = generateVerificationCode();
      verificationCodeRepository.save(email, verificationCode);
      sendEmail(email, verificationCode);
      return verificationCode;
    } catch (Exception e) {
      logger.error("Failed to send verification code email to {}: {}", email, e.getMessage());
      throw new RuntimeException("Failed to send verification code email", e);
    }
  }

  private String generateVerificationCode() {
    // 生成 4-6 位數字驗證碼
    return String.format("%06d", new Random().nextInt(900000) + 100000);
  }

  private void sendEmail(String email, String verificationCode) {
    try {
      // 建立郵件訊息
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message);
      helper.setFrom("devilwoops@gmail.com");
      helper.setTo(email);
      helper.setSubject("您的驗證碼");
      helper.setText("您的驗證碼是: " + verificationCode);

      // 發送郵件
      mailSender.send(message);
      logger.info("Verification code email sent to {}", email);
    } catch (MessagingException e) {
      logger.error("Failed to send verification code email to {}: {}", email, e.getMessage());
      throw new RuntimeException("Failed to send verification code email", e);
    }
  }
}