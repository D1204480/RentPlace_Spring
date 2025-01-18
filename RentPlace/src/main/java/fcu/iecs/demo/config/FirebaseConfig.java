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

  @Value("${spring.profiles.active:prod}")
  private String activeProfile;

  @PostConstruct
  public void initialize() {
    try {
      log.info("Starting Firebase initialization...");
      log.info("Active profile: {}", activeProfile);
      log.info("Firebase credentials length: {}",
          firebaseCredentials != null ? firebaseCredentials.length() : "null");

      // 檢查 credentials 內容的前100個字符（避免洩露敏感信息）
      if (!StringUtils.isEmpty(firebaseCredentials)) {
        log.info("Credentials preview: {}",
            firebaseCredentials.substring(0, Math.min(100, firebaseCredentials.length())));
      }

      if (!StringUtils.isEmpty(firebaseCredentials)) {
        log.info("Using environment credentials");
        initWithEnvCredentials();
      } else {
        log.error("Firebase credentials not found");
        throw new IllegalStateException("Firebase credentials not found");
      }
    } catch (Exception e) {
      log.error("Firebase initialization error: ", e);
      throw new RuntimeException("Firebase initialization failed: " + e.getMessage(), e);
    }
  }

  private void initWithEnvCredentials() throws IOException {
    try {
      log.info("Starting initialization with environment credentials");

      // 嘗試解析 JSON 來驗證格式
      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonNode = mapper.readTree(firebaseCredentials);
      log.info("JSON validation successful. Found fields: {}",
          StreamSupport.stream(jsonNode.fieldNames().spliterator(), false)
              .collect(Collectors.joining(", ")));

      InputStream serviceAccount = new ByteArrayInputStream(
          firebaseCredentials.getBytes(StandardCharsets.UTF_8)
      );
      initializeFirebase(serviceAccount);
      log.info("Environment credentials initialization complete");
    } catch (Exception e) {
      log.error("Error in initWithEnvCredentials: ", e);
      throw new IOException("Failed to initialize with environment credentials: " + e.getMessage(), e);
    }
  }

  private void initializeFirebase(InputStream serviceAccount) throws IOException {
    try {
      log.info("Creating Firebase options");
      FirebaseOptions options = FirebaseOptions.builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .build();

      if (FirebaseApp.getApps().isEmpty()) {
        log.info("Initializing new Firebase App");
        FirebaseApp.initializeApp(options);
        log.info("Firebase App initialization successful");
      } else {
        log.info("Firebase App already initialized");
      }
    } catch (Exception e) {
      log.error("Error in initializeFirebase: ", e);
      throw new IOException("Failed to initialize Firebase: " + e.getMessage(), e);
    }
  }
}