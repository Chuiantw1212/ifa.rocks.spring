package rocks.ifa.spring.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRetirementMapper {
    void deleteByUid(String uid);
}
