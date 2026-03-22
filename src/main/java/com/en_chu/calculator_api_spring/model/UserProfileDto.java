package com.en_chu.calculator_api_spring.model;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "個人基本資料更新請求 (Request DTO)")
public class UserProfileDto extends BaseDto {
	@Schema(description = "出生年份", example = "1990")
	@NotNull(message = "出生年份不能為空")
	@Min(value = 1900, message = "出生年份必須大於 1900")
	private Integer birthYear;

	@Schema(description = "出生日期 (ISO 8601)", example = "1990-12-12")
	@NotNull(message = "出生日期不能為空")
	@Past(message = "出生日期必須是過去的時間")
	private LocalDate birthDate;

	@Schema(description = "性別 (MALE/FEMALE)", example = "MALE")
	@NotBlank(message = "性別不能為空")
	@Pattern(regexp = "^(MALE|FEMALE)$", message = "性別必須是 MALE 或 FEMALE")
	private String gender;

	@Schema(description = "當前年齡", example = "35")
	@NotNull(message = "當前年齡不能為空")
	@Min(value = 0, message = "年齡不能小於 0")
	@Max(value = 150, message = "年齡數值異常")
	private Integer currentAge;

	@Schema(description = "預期壽命", example = "85")
	@NotNull(message = "預期壽命不能為空")
	@Min(value = 0, message = "預期壽命不能小於 0")
	private Integer lifeExpectancy;

	@Schema(description = "結婚年份", example = "2020")
	@Pattern(regexp = "^$|^\\d{4}$", message = "結婚年份格式錯誤 (YYYY)")
	private Integer marriageYear;

	@Schema(description = "職業保險類別代碼", example = "LABOR")
	@NotBlank(message = "職業保險類別不能為空")
	private String careerInsuranceType;

	@Schema(description = "個人簡介 / 理財故事")
	@Size(max = 2000, message = "個人簡介不能超過 2000 字")
	private String biography;
}