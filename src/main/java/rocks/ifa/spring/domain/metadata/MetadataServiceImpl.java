package rocks.ifa.spring.domain.metadata;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture; // Added the missing import
import com.google.cloud.firestore.*;
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
            if (LIFE_TABLE_COLLECTION.equals(filename)) {
                log.info("  - Skipping special file: {}", filename);
                continue;
            }
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

    @Override
    public void syncLifeTable() {
        try {
            // Use the injected resourceResolver instead of creating a new one
            Resource[] resources = resourceResolver.getResources("classpath:metadata/*.json");
            log.info("📂 [LifeTable] Searching for life table config file...");
            boolean found = false;
            for (Resource resource : resources) {
                try (InputStream is = resource.getInputStream()) {
                    Map<String, Object> data = objectMapper.readValue(is, new TypeReference<Map<String, Object>>() {});
                    String docId = (String) data.get("id");
                    if (LIFE_TABLE_COLLECTION.equals(docId)) {
                        found = true;
                        log.info("🚀 Found life table file, starting batch update process...");
                        processLifeTableBatch(docId, data);
                        break;
                    }
                }
            }
            if (!found) {
                log.warn("⚠️ Could not find config file with id '{}'", LIFE_TABLE_COLLECTION);
            }
        } catch (Exception e) {
            log.error("❌ LifeTable sync failed: ", e);
            throw new RuntimeException("LifeTable Sync Failed", e);
        }
    }

    private void processLifeTableBatch(String collectionName, Map<String, Object> sourceData) throws Exception {
        List<Map<String, Object>> list = objectMapper.convertValue(sourceData.get("list"),
                new TypeReference<List<Map<String, Object>>>() {
                });

        if (list == null || list.isEmpty()) {
            log.warn("生命表 list 為空，不進行寫入");
            return;
        }

        log.info("📊 準備處理 {} 筆生命表數據...", list.size());

        WriteBatch batch = firestore.batch();
        int batchCount = 0;
        int totalCount = 0;

        for (Map<String, Object> row : list) {
            Integer year = (Integer) row.get("year");
            String gender = (String) row.get("gender");
            Integer age = (Integer) row.get("age");

            Object lifespanObj = row.get("expected_lifespan");
            Double lifespan = (lifespanObj instanceof Number) ? ((Number) lifespanObj).doubleValue() : 0.0;

            String docKey = year + "_" + gender + "_" + age;

            Map<String, Object> docData = new HashMap<>();
            docData.put("year", year);
            docData.put("gender", gender);
            docData.put("age", age);
            docData.put("expected_lifespan", lifespan);

            DocumentReference docRef = firestore.collection(collectionName).document(docKey);
            batch.set(docRef, docData);

            batchCount++;
            totalCount++;

            if (batchCount >= 500) {
                batch.commit().get();
                batch = firestore.batch();
                batchCount = 0;
            }
        }

        if (batchCount > 0) {
            batch.commit().get();
        }

        log.info("✨ 生命表同步完成！(已將 {} 筆資料更新至 {})", totalCount, collectionName);
    }
}
