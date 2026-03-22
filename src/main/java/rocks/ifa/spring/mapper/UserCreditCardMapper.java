package rocks.ifa.spring.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserCreditCardMapper {
    void deleteByUid(String uid);
}
