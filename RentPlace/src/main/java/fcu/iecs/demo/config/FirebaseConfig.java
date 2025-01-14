package fcu.iecs.demo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FirebaseConfig {

    public FirebaseConfig() throws IOException {
        // 使用 ClassPathResource 加載服務帳戶密鑰
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new ClassPathResource("serviceAccountKey.json").getInputStream()
        );

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        // 防止多次初始化
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}
