package rocks.ifa.spring.domain.metadata;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetadataServiceImpl implements MetadataService {

    private final Firestore firestore;
    private final ObjectMapper objectMapper;
    private final ResourcePatternResolver resourceResolver;
    private static final String LIFE_TABLE_COLLECTION = "opt_life_table";

    @Override
    public Map<String, Object> getAllMetadata() {
        Map<String, Object> allMetadata = new HashMap<>();
        try {
            ApiFuture<QuerySnapshot> future = firestore.collection("metadata").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                allMetadata.put(document.getId(), document.getData());
            }
            log.info("Successfully fetched {} metadata configs", allMetadata.size());
        } catch (Exception e) {
            log.error("Failed to fetch Firebase Metadata", e);
        }
        return allMetadata;
    }

    @Override
    public MetadataContracts.LifeExpectancyRes getLifeExpectancy(Integer year, String gender, Integer age) {
        String ageSuffix = (age >= 100) ? "null" : String.valueOf(age);
        String docKey = year + "_" + gender + "_" + ageSuffix;

        try {
            DocumentSnapshot doc = firestore.collection(LIFE_TABLE_COLLECTION).document(docKey).get().get();

            if (doc.exists()) {
                Double val = doc.getDouble("expected_lifespan");
                BigDecimal lifespan = (val != null) ? BigDecimal.valueOf(val) : BigDecimal.ZERO;
                return new MetadataContracts.LifeExpectancyRes(year, gender, age, lifespan);
            } else {
                log.warn("Life expectancy data not found for key: {}", docKey);
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error fetching from Firestore: ", e);
            Thread.currentThread().interrupt();
            throw new RuntimeException("Database connection error");
        }
    }

    @Override
    public void syncMetadata() throws IOException {
        log.info("🚀 Starting metadata sync to Firestore...");
        Resource[] resources = resourceResolver.getResources("classpath:metadata/*.json");
        log.info("Found {} metadata files to sync.", resources.length);

        for (Resource resource : resources) {
            String filename = resource.getFilename();
            log.info("  - Processing file: {}", filename);
            try (InputStream inputStream = resource.getInputStream()) {
                Map<String, Object> data = objectMapper.readValue(inputStream, new TypeReference<>() {});
                String docId = (String) data.get("id");
                if (docId == null || docId.isBlank()) {
                    log.warn("    ⚠️ Skipping file {} because it does not contain an 'id' field.", filename);
                    continue;
                }
                firestore.collection("metadata").document(docId).set(data).get();
                log.info("    ✅ Successfully synced document with ID: {}", docId);
            } catch (Exception e) {
                log.error("    ❌ Failed to process or sync file: {}", filename, e);
            }
        }
        log.info("🏁 Metadata sync finished.");
    }
}
