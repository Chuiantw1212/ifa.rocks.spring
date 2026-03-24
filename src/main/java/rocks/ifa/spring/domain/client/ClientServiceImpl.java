package rocks.ifa.spring.domain.client;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.UUID;

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
    private final ClientProfileRepository clientProfileRepository;
    private final FirebaseAuth firebaseAuth;

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
    @Transactional
    public ClientProfileContracts.ProfileRes createClient(ClientContracts.CreateClientReq req) {
        log.info("Attempting to create a new client and Firebase account for email: {}", req.email());

        try {
            // 1. Create Firebase User
            UserRecord.CreateRequest firebaseReq = new UserRecord.CreateRequest()
                    .setEmail(req.email())
                    .setDisplayName(req.name())
                    .setPassword(generateRandomPassword()) // Generate a secure random password
                    .setEmailVerified(false)
                    .setDisabled(false);

            UserRecord userRecord = firebaseAuth.createUser(firebaseReq);
            log.info("Successfully created new Firebase user: {}", userRecord.getUid());

            // 2. Create local ClientProfileEntity
            ClientProfileEntity newProfile = new ClientProfileEntity();
            newProfile.setFirebaseUid(userRecord.getUid());
            // The 'name' is part of the biography in the new entity structure, let's adapt
            // newProfile.setBiography("Client Name: " + req.name());
            
            ClientProfileEntity savedProfile = clientProfileRepository.save(newProfile);
            log.info("✅ Successfully created new client profile with ID: {}", savedProfile.getId());

            // 3. Convert to response DTO
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

        } catch (FirebaseAuthException e) {
            log.error("❌ Failed to create Firebase user for email: {}", req.email(), e);
            throw new RuntimeException("Failed to create Firebase user: " + e.getMessage(), e);
        }
    }
    
    private String generateRandomPassword() {
        // In a real application, this should be a more secure and robust implementation.
        return UUID.randomUUID().toString().substring(0, 12);
    }
}
