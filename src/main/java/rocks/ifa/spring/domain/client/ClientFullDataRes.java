package rocks.ifa.spring.domain.client;

import lombok.Data;

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
