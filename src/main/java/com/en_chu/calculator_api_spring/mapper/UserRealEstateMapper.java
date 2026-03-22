package com.en_chu.calculator_api_spring.mapper;

import com.en_chu.calculator_api_spring.entity.UserRealEstate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserRealEstateMapper {

    int insert(UserRealEstate entity);

    int update(UserRealEstate entity);

    List<UserRealEstate> selectByUid(@Param("firebaseUid") String firebaseUid);

    Optional<UserRealEstate> selectByIdAndUid(@Param("id") Long id, @Param("firebaseUid") String firebaseUid);

    int deleteByIdAndUid(@Param("id") Long id, @Param("firebaseUid") String firebaseUid);

    int deleteByUid(@Param("firebaseUid") String firebaseUid);
}
