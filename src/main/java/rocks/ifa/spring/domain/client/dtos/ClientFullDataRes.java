package rocks.ifa.spring.domain.client.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import rocks.ifa.spring.domain.clientCareer.dtos.CareerRes;
import rocks.ifa.spring.domain.clientLaborInsurance.dtos.LaborInsuranceRes;
import rocks.ifa.spring.domain.clientLaborPension.dtos.LaborPensionRes;
import rocks.ifa.spring.domain.clientProfile.dtos.ProfileRes;
import rocks.ifa.spring.domain.clientRetirement.dtos.RetirementRes;
import rocks.ifa.spring.domain.clientTax.dtos.TaxRes;

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
