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
@Slf4j  // 如果有使用 Lombok
public class FirebaseConfig {
  @Value("${FIREBASE_CREDENTIALS}")
  private String firebaseCredentials;

  @PostConstruct
  public void initialize() {
    try {
      if (StringUtils.isNotEmpty(firebaseCredentials)) {
        log.info("Initializing Firebase with credentials from environment variable");
        initializeWithEnvCredentials();
      } else {
        log.info("Initializing Firebase with local credentials file");
        initializeWithLocalFile();
      }
    } catch (IOException e) {
      log.error("Failed to initialize Firebase: {}", e.getMessage(), e);
      throw new RuntimeException("Firebase initialization failed", e);
    }
  }

  private void initializeWithEnvCredentials() throws IOException {
    InputStream serviceAccount = new ByteArrayInputStream(firebaseCredentials.getBytes(StandardCharsets.UTF_8));
    initializeFirebase(serviceAccount);
  }

  private void initializeWithLocalFile() throws IOException {
    ClassPathResource resource = new ClassPathResource("serviceAccountKey.json");
    initializeFirebase(resource.getInputStream());
  }

  private void initializeFirebase(InputStream serviceAccount) throws IOException {
    FirebaseOptions options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build();

    if (FirebaseApp.getApps().isEmpty()) {
      FirebaseApp.initializeApp(options);
      log.info("Firebase has been initialized successfully");
    }
  }
}