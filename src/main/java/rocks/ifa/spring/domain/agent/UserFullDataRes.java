package rocks.ifa.spring.domain.agent;

import lombok.Data;
import rocks.ifa.spring.domain.clientCareer.ClientCareerRes;
import rocks.ifa.spring.domain.clientLaborInsurance.ClientLaborInsuranceRes;
import rocks.ifa.spring.domain.clientLaborPension.ClientLaborPensionRes;
import rocks.ifa.spring.domain.clientProfile.ClientProfileDto;
import rocks.ifa.spring.domain.clientRetirement.ClientRetirementRes;
import rocks.ifa.spring.domain.clientTax.ClientTaxRes;

@Data
public class UserFullDataRes {
    private Long id;
    private ClientProfileDto profile;
    private ClientCareerRes career;
    private ClientLaborPensionRes laborPension;
    private ClientLaborInsuranceRes laborInsurance;
    private ClientRetirementRes retirement;
    private ClientTaxRes tax;
}
