package rocks.ifa.spring.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserBusinessMapper {
    void deleteByUid(String uid);
}
