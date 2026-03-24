package rocks.ifa.spring.domain.clientProfile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientProfileServiceImpl implements ClientProfileService {

    private final ClientProfileRepository clientProfileRepository;

    @Override
    public ClientProfileContracts.ProfileRes getProfile(String uid) {
        return clientProfileRepository.findByAgentFirebaseUid(uid)
                .map(this::convertToRes)
                .orElseGet(() -> createDefaultProfile(uid));
    }

    @Override
    @Transactional
    public void updateProfile(String uid, ClientProfileContracts.UpdateProfileReq req) {
        ClientProfileEntity entity = clientProfileRepository.findByAgentFirebaseUid(uid)
                .orElseGet(() -> {
                    log.info("No existing profile for update, creating new one for UID: {}", uid);
                    ClientProfileEntity newProfile = new ClientProfileEntity();
                    newProfile.setAgentFirebaseUid(uid);
                    return newProfile;
                });

        // Manual mapping from record to entity
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

    private ClientProfileContracts.ProfileRes createDefaultProfile(String uid) {
        ClientProfileEntity newProfile = new ClientProfileEntity();
        newProfile.setAgentFirebaseUid(uid);
        clientProfileRepository.save(newProfile);
        log.info("✅ Minimal default profile created for UID: {}", uid);
        return convertToRes(newProfile);
    }

    private ClientProfileContracts.ProfileRes convertToRes(ClientProfileEntity entity) {
        return new ClientProfileContracts.ProfileRes(
            entity.getId(),
            entity.getBirthDate(),
            entity.getGender(),
            entity.getCurrentAge(),
            entity.getLifeExpectancy(),
            entity.getMarriageYear(),
            entity.getCareerInsuranceType(),
            entity.getBiography()
        );
    }
}
