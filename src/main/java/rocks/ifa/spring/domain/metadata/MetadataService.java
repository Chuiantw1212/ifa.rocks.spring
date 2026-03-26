package rocks.ifa.spring.domain.metadata;

import rocks.ifa.spring.domain.metadata.contracts.LifeExpectancyRes;

import java.io.IOException;
import java.util.Map;

public interface MetadataService {

    Map<String, Object> getAllMetadata();

    LifeExpectancyRes getLifeExpectancy(Integer year, String gender, Integer age);

    void syncMetadata() throws IOException;

    void syncLifeTable();
}
