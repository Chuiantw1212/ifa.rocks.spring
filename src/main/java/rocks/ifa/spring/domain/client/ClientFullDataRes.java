package rocks.ifa.spring.domain.client;

import lombok.Data;
import rocks.ifa.spring.domain.clientCareer.ClientCareerContracts;
import rocks.ifa.spring.domain.clientLaborInsurance.ClientLaborInsuranceContracts;
import rocks.ifa.spring.domain.clientLaborPension.ClientLaborPensionContracts;
import rocks.ifa.spring.domain.clientProfile.ClientProfileContracts;
import rocks.ifa.spring.domain.clientRetirement.ClientRetirementContracts;
import rocks.ifa.spring.domain.clientTax.ClientTaxContracts;

import java.util.UUID;

@Data
public class ClientFullDataRes {
    
    private UUID id;
    private ClientProfileContracts.ProfileRes profile;
    private ClientCareerContracts.CareerRes career;
    private ClientLaborPensionContracts.LaborPensionRes laborPension;
    private ClientLaborInsuranceContracts.LaborInsuranceRes laborInsurance;
    private ClientRetirementContracts.RetirementRes retirement;
    private ClientTaxContracts.TaxRes tax;
}
