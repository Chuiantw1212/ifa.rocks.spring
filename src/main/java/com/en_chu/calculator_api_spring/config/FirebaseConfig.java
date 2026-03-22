package com.en_chu.calculator_api_spring.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
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
    public Firestore firestore() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions.Builder optionsBuilder = FirebaseOptions.builder();

            // K_SERVICE 是 Google Cloud Run 保證會設定的標準環境變數。
            if (System.getenv("K_SERVICE") != null) {
                log.info("☁️ Cloud Run 環境已檢測。使用 ADC 並明確設定 Project ID。");

                // 這是最穩健的作法：同時提供 ADC 憑證和明確的 Project ID。
                optionsBuilder
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .setProjectId("enchu-8085a"); // 根據你的資訊，明確設定 Project ID

            } else {
                log.info("🏠 本地環境已檢測。從 Classpath 讀取 'service_account_key.json'。");
                // 本地開發邏輯不變，金鑰檔案中已包含 Project ID。
                InputStream serviceAccount = new ClassPathResource("service_account_key.json").getInputStream();
                optionsBuilder.setCredentials(GoogleCredentials.fromStream(serviceAccount));
            }

            FirebaseApp.initializeApp(optionsBuilder.build());
        }

        return FirestoreClient.getFirestore();
    }
}
