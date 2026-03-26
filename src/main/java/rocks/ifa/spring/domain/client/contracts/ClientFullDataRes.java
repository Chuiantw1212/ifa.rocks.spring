package rocks.ifa.spring.domain.client.contracts;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "客戶的完整理財資料聚合回應")
public record ClientFullDataRes(
    UUID id,
    ClientProfileContracts.ProfileRes profile,
    ClientCareerContracts.CareerRes career,
    ClientLaborPensionContracts.LaborPensionRes laborPension,
    ClientLaborInsuranceContracts.LaborInsuranceRes laborInsurance,
    ClientRetirementContracts.RetirementRes retirement,
    ClientTaxContracts.TaxRes tax
) {}
