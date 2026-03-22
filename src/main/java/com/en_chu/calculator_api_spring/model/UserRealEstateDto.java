package com.en_chu.calculator_api_spring.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserRealEstateDto extends BaseDto {

	// ==========================================
	// 基本資料
	// ==========================================

	@Schema(description = "物件名稱", example = "板橋自用宅")
	private String name = "新房產 (未命名)";

	@Schema(description = "屋齡 (年)", example = "10")
	@Min(value = 0, message = "屋齡不能小於 0")
	private Integer age = 0;

	// ==========================================
	// 價值與坪數 (計算基礎)
	// ==========================================

	@Schema(description = "權狀坪數", example = "35.5")
	@DecimalMin(value = "0.0", message = "坪數不能小於 0")
	private BigDecimal size = BigDecimal.ZERO;

	@Schema(description = "單價 (萬/坪)", example = "60.5")
	@DecimalMin(value = "0.0", message = "單價不能小於 0")
	private BigDecimal pricePerPing = BigDecimal.ZERO;

	// 🔒 系統計算欄位
	@Schema(description = "總價 (系統自動計算：單價 * 坪數)", example = "0", accessMode = Schema.AccessMode.READ_ONLY)
	@JsonProperty(access = Access.READ_ONLY)
	private BigDecimal totalPrice = BigDecimal.ZERO;

	// ==========================================
	// 稅務與貸款
	// ==========================================

	@Schema(description = "公告/評定現值 (稅基)", example = "800.0")
	@DecimalMin(value = "0.0", message = "公告現值不能小於 0")
	private BigDecimal assessedValue = BigDecimal.ZERO;

	@Schema(description = "預估持有稅率 (%)", example = "1.2")
	@DecimalMin(value = "0.0", message = "稅率不能小於 0")
	private BigDecimal holdingTaxRate = BigDecimal.ZERO;

	// ✅ [新增] 實際支付房屋稅/持有成本
	@Schema(description = "實際支付房屋稅 (年) - 用於核對與精準計算", example = "15000")
	@DecimalMin(value = "0.0", message = "實際持有成本不能小於 0")
	private BigDecimal actualHoldingCost = BigDecimal.ZERO;

	@Schema(description = "銀行貸款餘額 (萬)", example = "1000.0")
	@DecimalMin(value = "0.0", message = "貸款餘額不能小於 0")
	private BigDecimal loanAmount = BigDecimal.ZERO;

	@Schema(description = "年利率 (%)", example = "2.1")
	@DecimalMin(value = "0.0", message = "利率不能小於 0")
	private BigDecimal interestRate = BigDecimal.ZERO;

	// ==========================================
	// 使用狀態 (Enum 驗證)
	// ==========================================

	@Schema(description = "用途狀態 (self:自用, rent:出租, vacant:閒置)", example = "self")
	@Pattern(regexp = "^(self|rent|vacant)$", message = "用途狀態必須為: self, rent, 或 vacant")
	private String usageType = "self";

	@Schema(description = "月租金收入 (僅在 rent 狀態有效)", example = "25000")
	@DecimalMin(value = "0.0", message = "租金不能小於 0")
	private BigDecimal monthlyRent = BigDecimal.ZERO;
}