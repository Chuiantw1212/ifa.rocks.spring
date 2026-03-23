package rocks.ifa.spring.domain.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rocks.ifa.spring.domain.clientCareer.ClientCareerContracts;
import rocks.ifa.spring.domain.clientCareer.ClientCareerService;
import rocks.ifa.spring.domain.clientLaborInsurance.ClientLaborInsuranceContracts;
import rocks.ifa.spring.domain.clientLaborInsurance.ClientLaborInsuranceService;
import rocks.ifa.spring.domain.clientLaborPension.ClientLaborPensionContracts;
import rocks.ifa.spring.domain.clientLaborPension.ClientLaborPensionService;
import rocks.ifa.spring.domain.clientProfile.ClientProfileContracts;
import rocks.ifa.spring.domain.clientProfile.ClientProfileService;
import rocks.ifa.spring.domain.clientRetirement.ClientRetirementContracts;
import rocks.ifa.spring.domain.clientRetirement.ClientRetirementService;
import rocks.ifa.spring.domain.clientTax.ClientTaxContracts;
import rocks.ifa.spring.domain.clientTax.ClientTaxService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientProfileService clientProfileService;
    private final ClientCareerService clientCareerService;
    private final ClientLaborPensionService clientLaborPensionService;
    private final ClientLaborInsuranceService clientLaborInsuranceService;
    private final ClientRetirementService clientRetirementService;
    private final ClientTaxService clientTaxService;

    @Override
    public ClientFullDataRes getClientFullData(String uid) {
        log.info("🔍 [Aggregate Service] Assembling full client data for UID: {}", uid);
        
        ClientFullDataRes response = new ClientFullDataRes();

        ClientProfileContracts.ProfileRes profile = clientProfileService.getProfile(uid);
        ClientCareerContracts.CareerRes career = clientCareerService.getCareer(uid);
        ClientLaborPensionContracts.LaborPensionRes laborPension = clientLaborPensionService.getLaborPension(uid);
        ClientLaborInsuranceContracts.LaborInsuranceRes laborInsurance = clientLaborInsuranceService.getLaborInsurance(uid);
        ClientRetirementContracts.RetirementRes retirement = clientRetirementService.getRetirement(uid);
        ClientTaxContracts.TaxRes tax = clientTaxService.getTax(uid);

        response.setProfile(profile);
        response.setCareer(career);
        response.setLaborPension(laborPension);
        response.setLaborInsurance(laborInsurance);
        response.setRetirement(retirement);
        response.setTax(tax);

        if (profile != null) {
            response.setId(profile.id());
        }

        log.info("✅ [Aggregate Service] Full client data assembled successfully for UID: {}", uid);
        return response;
    }
}
