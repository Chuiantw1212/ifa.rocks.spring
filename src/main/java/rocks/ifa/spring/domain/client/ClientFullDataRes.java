package rocks.ifa.spring.domain.client;

import lombok.Data;
import rocks.ifa.spring.domain.clientCareer.ClientCareerRes;
import rocks.ifa.spring.domain.clientLaborInsurance.ClientLaborInsuranceRes;
import rocks.ifa.spring.domain.clientLaborPension.ClientLaborPensionRes;
import rocks.ifa.spring.domain.clientProfile.ClientProfileDto;
import rocks.ifa.spring.domain.clientRetirement.ClientRetirementRes;
import rocks.ifa.spring.domain.clientTax.ClientTaxRes;

/**
 * A comprehensive DTO that aggregates all financial data for a client.
 * This serves as the main data model for the client's dashboard view.
 */
@Data
public class ClientFullDataRes {
    
    private Long id;
    private ClientProfileDto profile;
    private ClientCareerRes career;
    private ClientLaborPensionRes laborPension;
    private ClientLaborInsuranceRes laborInsurance;
    private ClientRetirementRes retirement;
    private ClientTaxRes tax;
}
