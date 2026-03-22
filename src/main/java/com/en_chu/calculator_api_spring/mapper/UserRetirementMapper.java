package com.en_chu.calculator_api_spring.mapper;

import com.en_chu.calculator_api_spring.entity.UserRetirement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRetirementMapper {

    int insert(UserRetirement record);

    int updateByUid(UserRetirement record);
    
    int updateSelectiveByUid(UserRetirement record);

    UserRetirement selectByUid(@Param("firebaseUid") String firebaseUid);

    int deleteByUid(@Param("firebaseUid") String firebaseUid);

    boolean existsByUid(@Param("firebaseUid") String firebaseUid);
}
