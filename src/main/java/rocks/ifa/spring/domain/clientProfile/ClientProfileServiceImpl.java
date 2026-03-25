package rocks.ifa.spring.domain.clientProfile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.domain.metadata.MetadataService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rocks.ifa.spring.infra.common.PageResponse;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientProfileServiceImpl implements ClientProfileService {

    private final ClientProfileRepository clientProfileRepository;
    private final MetadataService metadataService; // Inject MetadataService

    @Override
    public ClientProfileContracts.ProfileRes getClientProfileById(UUID clientId) {
        log.info("Fetching client profile by ID: {}", clientId);
        return clientProfileRepository.findById(clientId)
                .map(this::convertToRes)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client profile not found"));
    }

    @Override
    public ClientProfileContracts.ProfileRes getProfile(String uid) {
        // This method's purpose seems to be finding a profile by agent UID,
        // which might not be what's intended for all use cases.
        // It's kept for now but might need re-evaluation.
        return clientProfileRepository.findByAgentFirebaseUid(uid)
                .map(this::convertToRes)
                .orElseGet(() -> createDefaultProfile(uid));
    }

    @Override
    @Transactional
    public void updateProfile(String uid, ClientProfileContracts.UpdateProfileReq req) {
        ClientProfileEntity entity = clientProfileRepository.findByAgentFirebaseUid(uid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client profile not found for the given agent UID"));

        // Update all fields from the PUT request
        entity.setName(req.name());
        entity.setEmail(req.email());
        entity.setPhone(req.phone());
        entity.setLineId(req.lineId());
        entity.setBirthDate(req.birthDate());
        entity.setGender(req.gender());
        entity.setMarriageYear(req.marriageYear());
        entity.setBiography(req.biography());
        entity.setCareerInsuranceType(req.careerInsuranceType());

        if (req.birthDate() != null) {
            entity.setCurrentAge(Period.between(req.birthDate(), LocalDate.now()).getYears());
        }

        clientProfileRepository.save(entity);
        log.info("✅ [Profile] Updated for user: {}", uid);
    }

    @Override
    @Transactional // This annotation is crucial for write operations
    public void patchProfile(UUID clientId, ClientProfileContracts.PatchProfileReq req) {
        log.info("Patching client profile for ID: {}", clientId);
        ClientProfileEntity entity = clientProfileRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client profile not found"));

        // Check each field and update only if it's not null
        if (req.name() != null) entity.setName(req.name());
        if (req.email() != null) entity.setEmail(req.email());
        if (req.phone() != null) entity.setPhone(req.phone());
        if (req.lineId() != null) entity.setLineId(req.lineId());
        if (req.birthDate() != null) {
            entity.setBirthDate(req.birthDate());
            entity.setCurrentAge(Period.between(req.birthDate(), LocalDate.now()).getYears());
        }
        if (req.gender() != null) entity.setGender(req.gender());
        if (req.marriageYear() != null) entity.setMarriageYear(req.marriageYear());
        if (req.careerInsuranceType() != null) entity.setCareerInsuranceType(req.careerInsuranceType());
        if (req.biography() != null) entity.setBiography(req.biography());

        clientProfileRepository.save(entity);
        log.info("✅ [Profile] Patched for client ID: {}", clientId);
    }

    @Override
    public PageResponse<ClientProfileContracts.ProfileRes> listClientProfilesByAgent(String agentUid, Pageable pageable) {
        Page<ClientProfileEntity> profilePage = clientProfileRepository.findAllByAgentFirebaseUid(agentUid, pageable);
        List<ClientProfileContracts.ProfileRes> dtoList = profilePage.getContent().stream()
                .map(this::convertToRes)
                .collect(Collectors.toList());
        return new PageResponse<>(
                dtoList,
                profilePage.getTotalElements(),
                profilePage.getNumber(),
                profilePage.getSize()
        );
    }

    private ClientProfileContracts.ProfileRes createDefaultProfile(String uid) {
        ClientProfileEntity newProfile = new ClientProfileEntity();
        newProfile.setId(UUID.randomUUID()); // Manually assign UUID for new entities
        newProfile.setAgentFirebaseUid(uid);
        // Set default non-null values to avoid database errors
        newProfile.setName("New Client");
        newProfile.setEmail(uid + "@default.com"); // Use UID to ensure uniqueness
        newProfile.setPhone("");
        newProfile.setLineId("");

        clientProfileRepository.save(newProfile);
        log.info("✅ Minimal default profile created for UID: {}", uid);
        return convertToRes(newProfile);
    }

    private ClientProfileContracts.ProfileRes convertToRes(ClientProfileEntity entity) {
        return new ClientProfileContracts.ProfileRes(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getLineId(),
                entity.getBirthDate(),
                entity.getGender(),
                entity.getCurrentAge(),
                entity.getLifeExpectancy(),
                entity.getMarriageYear(),
                entity.getCareerInsuranceType(),
                entity.getBiography()
        );
    }

    @Override
    @Transactional
    public ClientProfileContracts.ProfileRes updateProfile(UUID clientId, ClientProfileContracts.UpdateProfileReq req) {
        ClientProfileEntity entity = clientProfileRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client profile not found"));

        // ... (update other fields from req)

        updateLifeExpectancy(entity); // Call the new helper method

        ClientProfileEntity savedEntity = clientProfileRepository.save(entity);
        log.info("✅ [Profile] Updated for client ID: {}", clientId);
        return convertToRes(savedEntity);
    }

    @Override
    @Transactional
    public ClientProfileContracts.ProfileRes patchProfile(UUID clientId, ClientProfileContracts.PatchProfileReq req) {
        ClientProfileEntity entity = clientProfileRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client profile not found"));

        boolean needsLifeExpectancyUpdate = false;
        if (req.birthDate() != null) {
            entity.setBirthDate(req.birthDate());
            needsLifeExpectancyUpdate = true;
        }
        if (req.gender() != null) {
            entity.setGender(req.gender());
            needsLifeExpectancyUpdate = true;
        }
        // ... (update other fields from req)

        if (needsLifeExpectancyUpdate) {
            updateLifeExpectancy(entity);
        }

        ClientProfileEntity savedEntity = clientProfileRepository.save(entity);
        log.info("✅ [Profile] Patched for client ID: {}", clientId);
        return convertToRes(savedEntity);
    }

    private void updateLifeExpectancy(ClientProfileEntity entity) {
        if (entity.getBirthDate() == null || entity.getGender() == null) {
            return; // Not enough info to calculate
        }

        int currentAge = Period.between(entity.getBirthDate(), LocalDate.now()).getYears();
        entity.setCurrentAge(currentAge);

        // Assuming retirement age is 65 for this calculation
        int retirementAge = 65; 

        // Get current life expectancy
        var currentLifeExp = metadataService.getLifeExpectancy(LocalDate.now().getYear(), entity.getGender(), currentAge);
        if (currentLifeExp != null) {
            entity.setLifeExpectancy(currentLifeExp.expectedLifespan().intValue());
        }

        // Get remaining life expectancy at retirement
        var retirementLifeExp = metadataService.getLifeExpectancy(LocalDate.now().getYear(), entity.getGender(), retirementAge);
        if (retirementLifeExp != null) {
            entity.setLifeExpectancyAtRetirement(retirementLifeExp.expectedLifespan().intValue());
        }
    }

    private ClientProfileContracts.ProfileRes convertToRes(ClientProfileEntity entity) {
        return new ClientProfileContracts.ProfileRes(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getLineId(),
            entity.getBirthDate(),
            entity.getGender(),
            entity.getCurrentAge(),
            entity.getLifeExpectancy(),
            entity.getLifeExpectancyAtRetirement(),
            entity.getMarriageYear(),
            entity.getCareerInsuranceType(),
            entity.getBiography()
        );
    }
    
    // ... other preserved methods
}
