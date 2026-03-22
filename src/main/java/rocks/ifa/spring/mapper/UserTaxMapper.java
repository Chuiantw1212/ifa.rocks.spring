package rocks.ifa.spring.mapper;

import rocks.ifa.spring.entity.UserTax;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserTaxMapper {

    UserTax selectByUid(@Param("firebaseUid") String firebaseUid);

    int insert(UserTax entity);

    int updateByUid(UserTax entity);

    boolean existsByUid(@Param("firebaseUid") String firebaseUid);

    int deleteByUid(@Param("firebaseUid") String firebaseUid);
}
