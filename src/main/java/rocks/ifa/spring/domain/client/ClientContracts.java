package rocks.ifa.spring.domain.client;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import rocks.ifa.spring.domain.clientCareer.ClientCareerContracts;
import rocks.ifa.spring.domain.clientLaborInsurance.ClientLaborInsuranceContracts;
import rocks.ifa.spring.domain.clientLaborPension.ClientLaborPensionContracts;
import rocks.ifa.spring.domain.clientProfile.ClientProfileContracts;
import rocks.ifa.spring.domain.clientRetirement.ClientRetirementContracts;
import rocks.ifa.spring.domain.clientTax.ClientTaxContracts;

import java.util.UUID;

public interface ClientContracts {

    @Schema(description = "用於建立新客戶的請求")
    record CreateClientReq(
            @Schema(description = "客戶姓名", example = "王大明")
            @NotBlank(message = "客戶姓名不能為空")
            String name,

            @Schema(description = "客戶 Email", example = "david.wang@example.com")
            @NotBlank(message = "Email 不能為空")
            @Email(message = "Email 格式不正確")
            String email,

            @Schema(description = "客戶電話", example = "0912345678")
            String phone,

            @Schema(description = "客戶 Line ID", example = "david_wang")
            String lineId
    ) {
    }

    @Schema(description = "客戶的完整理財資料聚合回應")
    record ClientFullDataRes(
            UUID id,
            ClientProfileContracts.ProfileRes profile,
            ClientCareerContracts.CareerRes career,
            ClientLaborPensionContracts.LaborPensionRes laborPension,
            ClientLaborInsuranceContracts.LaborInsuranceRes laborInsurance,
            ClientRetirementContracts.RetirementRes retirement,
            ClientTaxContracts.TaxRes tax
    ) {
    }
}
