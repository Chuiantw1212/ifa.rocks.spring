package rocks.ifa.spring.domain.metadata;

import rocks.ifa.spring.domain.metadata.dtos.LifeExpectancyRes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MetadataService {

    Map<String, Object> getAllMetadata();

    LifeExpectancyRes getLifeExpectancy(Integer year, String gender, Integer age);

    List<LifeExpectancyRes> getLifeExpectancyRange(String gender, int baseAge);

    void syncMetadata() throws IOException;

    void syncLifeTable();
}
