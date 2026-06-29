package rocks.ifa.spring.domain.clientCareer.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "用於更新客戶職涯資料的請求")
public record UpdateCareerReq(
    @Schema(description = "本薪") BigDecimal baseSalary,
    @Schema(description = "其他津貼") BigDecimal otherAllowance,
    @Schema(description = "勞保費") BigDecimal laborInsurance,
    @Schema(description = "健保費") BigDecimal healthInsurance,
    @Schema(description = "其他扣項") BigDecimal otherDeduction,
    @Schema(description = "個人勞退自提率 (%)") BigDecimal pensionPersonalRate,
    @Schema(description = "員工認股扣款") BigDecimal stockDeduction,
    @Schema(description = "公司相對提撥") BigDecimal stockCompanyMatch,
    @Schema(description = "年終與非經常性獎金 (預估)") BigDecimal annualBonus,
    @Schema(description = "健保扶養人數") Integer dependents
) {}
