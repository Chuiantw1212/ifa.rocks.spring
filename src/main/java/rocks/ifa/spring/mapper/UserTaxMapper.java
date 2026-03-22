package rocks.ifa.spring.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserTaxMapper {
    void deleteByUid(String uid);
}
