package com.en_chu.calculator_api_spring.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseDto {

	// 前端只能讀，不能改
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	@JsonProperty(access = Access.READ_ONLY)
	private Long id;

	// ✅ 這裡完全沒有 firebaseUid 欄位！
}