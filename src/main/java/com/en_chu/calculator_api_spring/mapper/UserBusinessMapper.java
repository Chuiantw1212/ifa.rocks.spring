package com.en_chu.calculator_api_spring.mapper;

import com.en_chu.calculator_api_spring.entity.UserBusiness;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserBusinessMapper {

    int insert(UserBusiness entity);

    int update(UserBusiness entity);

    List<UserBusiness> selectByUid(@Param("firebaseUid") String firebaseUid);

    Optional<UserBusiness> selectByIdAndUid(@Param("id") Long id, @Param("firebaseUid") String firebaseUid);

    List<UserBusiness> selectPage(@Param("firebaseUid") String firebaseUid, @Param("limit") int limit, @Param("offset") int offset);

    long countByUid(@Param("firebaseUid") String firebaseUid);

    int deleteByIdAndUid(@Param("id") Long id, @Param("firebaseUid") String firebaseUid);

    int deleteByUid(@Param("firebaseUid") String firebaseUid);
}
