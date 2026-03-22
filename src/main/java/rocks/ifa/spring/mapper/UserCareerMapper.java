package rocks.ifa.spring.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserCareerMapper {
    void deleteByUid(String uid);
}
