package rocks.ifa.spring.domain.metadata;

import java.io.IOException;
import java.util.Map;

public interface MetadataService {

    Map<String, Object> getAllMetadata();

    MetadataContracts.LifeExpectancyRes getLifeExpectancy(Integer year, String gender, Integer age);

    void syncMetadata() throws IOException;

    /**
     * Finds and syncs the life table data from a specific JSON file
     * in `resources/metadata` to its own collection in Firestore using batch writes.
     */
    void syncLifeTable();
}
