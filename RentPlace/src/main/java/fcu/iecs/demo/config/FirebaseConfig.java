package fcu.iecs.demo.config;

import com.google.api.client.util.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

//@Configuration
//public class FirebaseConfig {
//
//    public FirebaseConfig() throws IOException {
//        // 使用 ClassPathResource 加載服務帳戶密鑰
//        GoogleCredentials credentials = GoogleCredentials.fromStream(
//                new ClassPathResource("serviceAccountKey.json").getInputStream()
//        );
//
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(credentials)
//                .build();
//
//        // 防止多次初始化
//        if (FirebaseApp.getApps().isEmpty()) {
//            FirebaseApp.initializeApp(options);
//        }
//    }
//}

@Configuration
@Slf4j
public class FirebaseConfig {
  @Value("${FIREBASE_CREDENTIALS:}")
  private String firebaseCredentials;

  @Value("${spring.profiles.active:prod}")  // 默認使用 prod profile
  private String activeProfile;

  @PostConstruct
  public void initialize() {
    try {
      log.info("Active profile: {}", activeProfile);
      log.info("Firebase credentials exists: {}", !StringUtils.isEmpty(firebaseCredentials));

      if (!StringUtils.isEmpty(firebaseCredentials)) {
        log.info("Initializing Firebase with credentials from environment variable");
        initWithEnvCredentials();
      } else if ("dev".equals(activeProfile) || "local".equals(activeProfile)) {
        log.info("Development environment detected, using local file");
        initWithLocalFile();
      } else {
        String errorMessage = "Firebase credentials not found in environment variables";
        log.error(errorMessage);
        throw new IllegalStateException(errorMessage);
      }
    } catch (Exception e) {
      log.error("Firebase initialization failed", e);
      throw new RuntimeException("Firebase initialization failed", e);
    }
  }

  private void initWithEnvCredentials() throws IOException {
    try {
      log.info("Starting Firebase initialization with env credentials");
      InputStream serviceAccount = new ByteArrayInputStream(
          firebaseCredentials.getBytes(StandardCharsets.UTF_8)
      );
      initializeFirebase(serviceAccount);
      log.info("Firebase successfully initialized with env credentials");
    } catch (Exception e) {
      log.error("Error initializing Firebase with env credentials: {}", e.getMessage());
      throw e;
    }
  }

  private void initWithLocalFile() throws IOException {
    ClassPathResource resource = new ClassPathResource("serviceAccountKey.json");
    if (!resource.exists()) {
      throw new FileNotFoundException("serviceAccountKey.json not found in resources directory");
    }
    initializeFirebase(resource.getInputStream());
  }

  private void initializeFirebase(InputStream serviceAccount) throws IOException {
    try {
      FirebaseOptions options = FirebaseOptions.builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .build();

      if (FirebaseApp.getApps().isEmpty()) {
        FirebaseApp.initializeApp(options);
        log.info("Firebase App has been initialized successfully");
      }
    } catch (IOException e) {
      log.error("Error in initializeFirebase: {}", e.getMessage());
      throw e;
    }
  }
}