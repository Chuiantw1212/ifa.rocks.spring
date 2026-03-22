package com.en_chu.calculator_api_spring.mapper;

import com.en_chu.calculator_api_spring.entity.UserPortfolio;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserPortfolioMapper {

    int insert(UserPortfolio entity);

    int update(UserPortfolio entity);

    List<UserPortfolio> selectByUid(@Param("firebaseUid") String firebaseUid);

    Optional<UserPortfolio> selectByIdAndUid(@Param("id") Long id, @Param("firebaseUid") String firebaseUid);

    int deleteByIdAndUid(@Param("id") Long id, @Param("firebaseUid") String firebaseUid);

    int deleteByUid(@Param("firebaseUid") String firebaseUid);
}
