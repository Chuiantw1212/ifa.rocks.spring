package rocks.ifa.spring.application.metadata;

import rocks.ifa.spring.application.metadata.dtos.LifeExpectancyRes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MetadataApplicationService {

    Map<String, Object> getAllMetadata();

    LifeExpectancyRes getLifeExpectancy(Integer year, String gender, Integer age);

    List<LifeExpectancyRes> getLifeExpectancyRange(String gender, int baseAge, Integer year);

    void syncMetadata() throws IOException;

    void syncLifeTable();
}
