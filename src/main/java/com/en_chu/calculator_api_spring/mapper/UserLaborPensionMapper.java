package com.en_chu.calculator_api_spring.mapper;

import com.en_chu.calculator_api_spring.entity.UserLaborPension;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserLaborPensionMapper {

    UserLaborPension selectByUid(@Param("firebaseUid") String firebaseUid);

    int insert(UserLaborPension entity);

    int updateByUid(UserLaborPension entity);

    boolean existsByUid(@Param("firebaseUid") String firebaseUid);

    int deleteByUid(@Param("firebaseUid") String firebaseUid);
}
