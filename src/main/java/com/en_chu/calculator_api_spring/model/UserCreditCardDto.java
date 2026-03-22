package com.en_chu.calculator_api_spring.model;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserCreditCardDto extends BaseDto {

    @Schema(description = "卡片名稱", example = "玉山 U Bear")
    private String name; // 不再強制驗證，由 Service 補預設值

    @Schema(description = "扣款帳戶", example = "台新 Richart")
    private String deductionAccount;

    @Schema(description = "用途分類", example = "online")
    private String usageType;

    @Schema(description = "存放位置", example = "digital")
    private String storageLocation;

    @Schema(description = "平均月開支", example = "3500")
    @Min(value = 0)
    private BigDecimal averageMonthlyExpense;
}