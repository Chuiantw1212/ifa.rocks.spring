package rocks.ifa.spring.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserLaborInsuranceMapper {
    void deleteByUid(String uid);
}
