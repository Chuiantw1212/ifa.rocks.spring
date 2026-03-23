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
import rocks.ifa.spring.domain.clientProfile.ClientProfileEntity;
import rocks.ifa.spring.domain.clientProfile.ClientProfileRepository;
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

    @Override
    public ClientProfileContracts.ProfileRes createClient(ClientContracts.CreateClientReq req) {
        log.info("Creating new client with email: {}", req.email());

        // Here you might want to check if a client with this email already exists
        // if (clientProfileRepository.existsByEmail(req.email())) {
        //     throw new IllegalStateException("Client with this email already exists.");
        // }

        ClientProfileEntity newProfile = new ClientProfileEntity();
        // In a real app, you would link this to an agent, not a firebaseUid
        // newProfile.setFirebaseUid( ... );
        // newProfile.setName(req.name());
        // newProfile.setEmail(req.email());

        ClientProfileEntity savedProfile = clientProfileRepository.save(newProfile);
        log.info("✅ Successfully created new client with ID: {}", savedProfile.getId());

        // Convert entity to response DTO
        return new ClientProfileContracts.ProfileRes(
                savedProfile.getId(),
                savedProfile.getBirthDate(),
                savedProfile.getGender(),
                savedProfile.getCurrentAge(),
                savedProfile.getLifeExpectancy(),
                savedProfile.getMarriageYear(),
                savedProfile.getCareerInsuranceType(),
                savedProfile.getBiography()
        );
    }
}
