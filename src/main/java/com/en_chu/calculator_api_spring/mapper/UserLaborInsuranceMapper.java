package com.en_chu.calculator_api_spring.mapper;

import com.en_chu.calculator_api_spring.entity.UserLaborInsurance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserLaborInsuranceMapper {

	UserLaborInsurance selectByUid(@Param("firebaseUid") String firebaseUid);

	int insert(UserLaborInsurance entity);

	int updateByUid(UserLaborInsurance entity);

	int deleteByUid(@Param("firebaseUid") String firebaseUid);

	boolean existsByUid(@Param("firebaseUid") String firebaseUid);
}
