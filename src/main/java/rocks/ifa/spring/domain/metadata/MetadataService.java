package rocks.ifa.spring.domain.metadata;

import java.io.IOException;
import java.util.Map;

public interface MetadataService {

    Map<String, Object> getAllMetadata();

    MetadataContracts.LifeExpectancyRes getLifeExpectancy(Integer year, String gender, Integer age);

    /**
     * Syncs all local JSON files from `resources/metadata` to the Firestore 'metadata' collection.
     * @throws IOException if the resource files cannot be read.
     */
    void syncMetadata() throws IOException;
}
