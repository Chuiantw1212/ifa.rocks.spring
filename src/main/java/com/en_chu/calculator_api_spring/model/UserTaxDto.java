package com.en_chu.calculator_api_spring.model;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "稅務規劃專用參數")
public class UserTaxDto extends BaseDto {

	@Schema(description = "預估其他所得 (股利、利息、租金等需併入綜所稅之金額)", example = "50000")
	@Min(0)
	private BigDecimal estimatedOtherIncome;

}