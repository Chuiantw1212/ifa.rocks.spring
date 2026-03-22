package rocks.ifa.spring.model.dto;

import lombok.Data;

@Data
public class UserFullDataRes {
    private int id;
    private UserProfileRes profile;
    private UserCareerRes career;
    private UserLaborPensionRes laborPension;
    private UserLaborInsuranceRes laborInsurance;
    private UserRetirementRes retirement;
    private UserTaxRes tax;
}
