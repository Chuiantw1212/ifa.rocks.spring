package rocks.ifa.spring.mapper;

import rocks.ifa.spring.entity.UserCareer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserCareerMapper {

    UserCareer selectByUid(@Param("firebaseUid") String firebaseUid);

    int insert(UserCareer entity);

    int updateByUid(UserCareer entity);

    boolean existsByUid(@Param("firebaseUid") String firebaseUid);

    int deleteByUid(@Param("firebaseUid") String firebaseUid);
}
