package rocks.ifa.spring.domain.metadata;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetadataService {

    private final Firestore firestore;
    private static final String LIFE_TABLE_COLLECTION = "opt_life_table";

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
}
