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

  @Value("${spring.profiles.active:}")
  private String activeProfile;

  @PostConstruct
  public void initialize() {
    try {
      log.info("Current active profile: {}", activeProfile);

      if (StringUtils.isNotEmpty(firebaseCredentials)) {
        log.info("Using environment variable configuration");
        initWithEnvCredentials();
      } else {
        log.info("Trying to use local configuration file");
        initWithLocalFile();
      }
    } catch (Exception e) {
      String errorMessage = "Firebase configuration is missing. Please either:\n" +
          "1. Set FIREBASE_CREDENTIALS environment variable, or\n" +
          "2. Place serviceAccountKey.json in src/main/resources/";
      log.error(errorMessage, e);
      throw new IllegalStateException(errorMessage, e);
    }
  }

  private void initWithLocalFile() throws IOException {
    ClassPathResource resource = new ClassPathResource("serviceAccountKey.json");
    if (!resource.exists()) {
      throw new FileNotFoundException("serviceAccountKey.json not found in resources directory");
    }
    initializeFirebase(resource.getInputStream());
  }

  private void initWithEnvCredentials() throws IOException {
    if (StringUtils.isEmpty(firebaseCredentials)) {
      throw new IllegalStateException("FIREBASE_CREDENTIALS environment variable is empty");
    }
    InputStream serviceAccount = new ByteArrayInputStream(
        firebaseCredentials.getBytes(StandardCharsets.UTF_8)
    );
    initializeFirebase(serviceAccount);
  }

  private void initializeFirebase(InputStream serviceAccount) throws IOException {
    FirebaseOptions options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build();

    if (FirebaseApp.getApps().isEmpty()) {
      FirebaseApp.initializeApp(options);
      log.info("Firebase initialized successfully");
    }
  }
}