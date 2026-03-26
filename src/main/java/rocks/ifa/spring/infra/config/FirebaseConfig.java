package rocks.ifa.spring.infra.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions.Builder optionsBuilder = FirebaseOptions.builder();

            if (System.getenv("K_SERVICE") != null) {
                log.info("☁️ Cloud Run environment detected. Using Application Default Credentials.");
                optionsBuilder
                        .setCredentials(GoogleCredentials.getApplicationDefault())
                        .setProjectId("ifa-rocks");
            } else {
                log.info("🏠 Local environment detected. Reading 'service_account_key.json' from Classpath.");
                InputStream serviceAccount = new ClassPathResource("service_account_key.json").getInputStream();
                optionsBuilder.setCredentials(GoogleCredentials.fromStream(serviceAccount));
            }
            return FirebaseApp.initializeApp(optionsBuilder.build());
        }
        return FirebaseApp.getInstance();
    }

    @Bean
    public Firestore firestore(FirebaseApp firebaseApp) {
        return FirestoreClient.getFirestore(firebaseApp);
    }

    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }
}
