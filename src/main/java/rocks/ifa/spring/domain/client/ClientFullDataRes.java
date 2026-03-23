package rocks.ifa.spring.domain.client;

import lombok.Data;
import rocks.ifa.spring.domain.clientCareer.UserCareerRes;
import rocks.ifa.spring.domain.clientLaborInsurance.UserLaborInsuranceRes;
import rocks.ifa.spring.domain.clientLaborPension.UserLaborPensionRes;
import rocks.ifa.spring.domain.clientProfile.UserProfileDto;
import rocks.ifa.spring.domain.clientRetirement.UserRetirementRes;
import rocks.ifa.spring.domain.clientTax.UserTaxRes;

@Data
public class ClientFullDataRes {
    private Long id;
    private UserProfileDto profile;
    private UserCareerRes career;
    private UserLaborPensionRes laborPension;
    private UserLaborInsuranceRes laborInsurance;
    private UserRetirementRes retirement;
    private UserTaxRes tax;
}
