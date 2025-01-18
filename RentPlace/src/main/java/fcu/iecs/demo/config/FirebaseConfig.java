package fcu.iecs.demo.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

  private String getFirebaseCredentials() {
    // 直接從系統環境變數讀取
    String credentials = System.getenv("FIREBASE_CREDENTIALS");
    log.info("Reading credentials from System.getenv: {}", credentials != null ? "found" : "not found");
    return credentials;
  }

  @PostConstruct
  public void initialize() {
    try {
      String firebaseCredentials = getFirebaseCredentials();
      log.info("Starting Firebase initialization...");

      if (firebaseCredentials != null && !firebaseCredentials.trim().isEmpty()) {
        log.info("Found credentials, length: {}", firebaseCredentials.length());
        log.info("Credentials preview: {}",
            firebaseCredentials.substring(0, Math.min(100, firebaseCredentials.length())));

        InputStream serviceAccount = new ByteArrayInputStream(
            firebaseCredentials.getBytes(StandardCharsets.UTF_8)
        );

        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();

        if (FirebaseApp.getApps().isEmpty()) {
          FirebaseApp.initializeApp(options);
          log.info("Firebase initialization successful");
        }
      } else {
        log.error("Firebase credentials not found in environment variables");
        throw new IllegalStateException("Firebase credentials not found");
      }
    } catch (Exception e) {
      log.error("Firebase initialization error: ", e);
      throw new RuntimeException("Firebase initialization failed: " + e.getMessage(), e);
    }
  }
}