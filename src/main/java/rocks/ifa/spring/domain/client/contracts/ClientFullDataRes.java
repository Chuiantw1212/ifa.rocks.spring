package rocks.ifa.spring.domain.client.contracts;

import io.swagger.v3.oas.annotations.media.Schema;
import rocks.ifa.spring.domain.clientCareer.contracts.CareerRes;
import rocks.ifa.spring.domain.clientLaborInsurance.contracts.LaborInsuranceRes;
import rocks.ifa.spring.domain.clientLaborPension.contracts.LaborPensionRes;
import rocks.ifa.spring.domain.clientProfile.contracts.ProfileRes;
import rocks.ifa.spring.domain.clientRetirement.contracts.RetirementRes;
import rocks.ifa.spring.domain.clientTax.contracts.TaxRes;

import java.util.UUID;

@Schema(description = "客戶的完整理財資料聚合回應")
public record ClientFullDataRes(
    UUID id,
    ProfileRes profile,
    CareerRes career,
    LaborPensionRes laborPension,
    LaborInsuranceRes laborInsurance,
    RetirementRes retirement,
    TaxRes tax
) {}
