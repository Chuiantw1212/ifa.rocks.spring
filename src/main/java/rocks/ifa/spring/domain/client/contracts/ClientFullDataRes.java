package rocks.ifa.spring.domain.client.contracts;

import io.swagger.v3.oas.annotations.media.Schema;
import rocks.ifa.spring.domain.clientCareer.ClientCareerContracts;
import rocks.ifa.spring.domain.clientLaborInsurance.ClientLaborInsuranceContracts;
import rocks.ifa.spring.domain.clientLaborPension.ClientLaborPensionContracts;
import rocks.ifa.spring.domain.clientProfile.ClientProfileContracts;
import rocks.ifa.spring.domain.clientRetirement.ClientRetirementContracts;
import rocks.ifa.spring.domain.clientTax.ClientTaxContracts;

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
