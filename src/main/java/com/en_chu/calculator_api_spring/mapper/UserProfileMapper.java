package com.en_chu.calculator_api_spring.mapper;

import com.en_chu.calculator_api_spring.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserProfileMapper {

    UserProfile selectByUid(@Param("firebaseUid") String firebaseUid);

    int insert(UserProfile entity);

    int updateByUid(UserProfile entity);

    boolean existsByUid(@Param("firebaseUid") String firebaseUid);

    int deleteByUid(@Param("firebaseUid") String firebaseUid);

    boolean checkUserExists(@Param("firebaseUid") String firebaseUid);

    // ✅ 修正：接收一個 UserProfile 物件，以便 MyBatis 可以回填 ID
    void insertInitUser(UserProfile profile);

    void updateLastLogin(@Param("firebaseUid") String firebaseUid);
}
