package rocks.ifa.spring.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRealEstateMapper {
    void deleteByUid(String uid);
}
