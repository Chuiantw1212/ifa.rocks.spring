package com.en_chu.calculator_api_spring.model;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "使用者勞工保險 (勞保) 參數")
public class UserLaborInsuranceDto extends BaseDto {

	@NotNull(message = "預計請領年齡不能為空")
	@Min(value = 0, message = "年齡不能為負數")
	@Schema(description = "預計開始請領年齡 (法定請領年齡 - 5)", example = "60")
	private Integer expectedClaimAge;

	@Schema(description = "最高 60 個月之平均投保薪資", example = "45800")
	@Min(value = 0, message = "薪資不能為負數")
	private BigDecimal averageMonthlySalary;

	@Schema(description = "保險年資 (總月數)", example = "180")
	@Min(value = 0, message = "年資不能為負數")
	private Integer insuranceSeniority;
	
	@NotNull(message = "預估餘命不能為空")
    @Min(value = 0, message = "預估餘命不能為負數")
    @Schema(description = "請領時預估餘命 (年)", example = "20.5")
    private BigDecimal predictedRemainingLife;
	
	@Schema(description = "預估每月領取金額 (勞保年金)", example = "24500")
    @Min(value = 0)
    private BigDecimal predictedMonthlyAnnuity;
}