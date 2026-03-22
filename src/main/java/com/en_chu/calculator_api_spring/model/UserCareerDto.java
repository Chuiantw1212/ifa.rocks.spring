package com.en_chu.calculator_api_spring.model;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "更新職涯收入請求物件")
public class UserCareerDto extends BaseDto {
    @NotNull(message = "本薪不能為空")
    @Min(value = 0, message = "本薪不能為負數")
    @Schema(description = "本薪", example = "50000")
    private BigDecimal baseSalary;

    @Schema(description = "其他津貼", example = "2400")
    private BigDecimal otherAllowance;

    @Schema(description = "勞保費", example = "1100")
    private BigDecimal laborInsurance;

    @Schema(description = "健保費", example = "800")
    private BigDecimal healthInsurance;

    @Schema(description = "其他扣項", example = "100")
    private BigDecimal otherDeduction;

    @Schema(description = "個人勞退自提率 (%)", example = "6.0")
    private BigDecimal pensionPersonalRate;

    @Schema(description = "個人勞退自提金額", example = "3000")
    private BigDecimal pensionPersonalAmount;

    @Schema(description = "雇主勞退提繳金額 (通常為薪資 6%)", example = "3000")
    private BigDecimal pensionEmployerAmount;

    @Schema(description = "每月勞退投入總額 (個人+雇主)", example = "6000")
    private BigDecimal pensionTotalAmount;

    @Schema(description = "員工認股扣款", example = "2000")
    private BigDecimal stockDeduction;

    @Schema(description = "公司相對提撥", example = "1000")
    private BigDecimal stockCompanyMatch;

    @Schema(description = "每個月實領金額 (稅後)", example = "58000")
    @NotNull(message = "實領金額不能為空")
    @Min(value = 0, message = "金額不能為負數")
    private BigDecimal monthlyNetIncome;
    
    @Schema(description = "年終與非經常性獎金 (預估)", example = "120000")
    @Min(0)
    private BigDecimal annualBonus;

    @Schema(description = "全年總薪資 (用於階層判斷 D1-D10)", example = "780000")
    @Min(0)
    private BigDecimal annualTotalIncome;
    
    @Schema(description = "健保扶養人數 (眷屬人數)", example = "0")
    @Min(0)
    @Max(3) // 健保費計算上限通常為 3 人 (第 4 人以上免費)
    private Integer dependents;
}