package rocks.ifa.spring.domain.clientCareer.contracts;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "客戶的職涯與收入資料回應")
public record CareerRes(
    @Schema(description = "本薪") BigDecimal baseSalary,
    @Schema(description = "其他津貼") BigDecimal otherAllowance,
    @Schema(description = "勞保費") BigDecimal laborInsurance,
    @Schema(description = "健保費") BigDecimal healthInsurance,
    @Schema(description = "其他扣項") BigDecimal otherDeduction,
    @Schema(description = "個人勞退自提率 (%)") BigDecimal pensionPersonalRate,
    @Schema(description = "個人勞退自提金額") BigDecimal pensionPersonalAmount,
    @Schema(description = "雇主勞退提繳金額") BigDecimal pensionEmployerAmount,
    @Schema(description = "每月勞退投入總額") BigDecimal pensionTotalAmount,
    @Schema(description = "員工認股扣款") BigDecimal stockDeduction,
    @Schema(description = "公司相對提撥") BigDecimal stockCompanyMatch,
    @Schema(description = "每月實領金額 (稅後)") BigDecimal monthlyNetIncome,
    @Schema(description = "年終與非經常性獎金 (預估)") BigDecimal annualBonus,
    @Schema(description = "全年總薪資") BigDecimal annualTotalIncome,
    @Schema(description = "健保扶養人數") Integer dependents
) {}
