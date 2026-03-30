package rocks.ifa.spring.domain.clientProfile;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rocks.ifa.spring.domain.clientProfile.contracts.PatchProfileReq;
import rocks.ifa.spring.domain.clientProfile.contracts.ProfileRes;
import rocks.ifa.spring.domain.clientProfile.contracts.UpdateProfileReq;
import rocks.ifa.spring.domain.metadata.MetadataService;
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
    private final MetadataService metadataService;
    private final ClientProfileMapper clientProfileMapper;
    private final FirebaseAuth firebaseAuth;

    @Override
    public ProfileRes getOwnProfile(String clientFirebaseUid) {
        return clientProfileRepository.findByClientFirebaseUid(clientFirebaseUid)
                .map(clientProfileMapper::toProfileRes)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client profile not found for the current user."));
    }
    
    @Override
    public ProfileRes getClientProfileById(UUID clientId) {
        return clientProfileRepository.findById(clientId)
                .map(clientProfileMapper::toProfileRes)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client profile not found"));
    }

    @Override
    public ProfileRes getProfile(String uid) {
        return clientProfileRepository.findByAgentFirebaseUid(uid)
                .map(clientProfileMapper::toProfileRes)
                .orElseGet(() -> createDefaultProfile(uid));
    }

    @Override
    @Transactional
    public ProfileRes updateProfile(UUID clientId, UpdateProfileReq req) {
        ClientProfileEntity entity = clientProfileRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client profile not found"));

        entity.setName(req.name());
        entity.setEmail(req.email());
        entity.setPhone(req.phone());
        entity.setLineId(req.lineId());
        entity.setBirthDate(req.birthDate());
        entity.setGender(req.gender());
        entity.setRetirementAge(req.retirementAge());
        entity.setMarriageYear(req.marriageYear());
        entity.setBiography(req.biography());
        entity.setCareerInsuranceType(req.careerInsuranceType());

        updateLifeExpectancy(entity);
        ClientProfileEntity savedEntity = clientProfileRepository.save(entity);
        return clientProfileMapper.toProfileRes(savedEntity);
    }

    @Override
    @Transactional
    public ProfileRes patchProfile(UUID clientId, PatchProfileReq req) {
        ClientProfileEntity entity = clientProfileRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client profile not found"));

        boolean needsLifeExpectancyUpdate = false;
        if (req.name() != null) entity.setName(req.name());
        if (req.email() != null) entity.setEmail(req.email());
        if (req.phone() != null) entity.setPhone(req.phone());
        if (req.lineId() != null) entity.setLineId(req.lineId());
        if (req.birthDate() != null) {
            entity.setBirthDate(req.birthDate());
            needsLifeExpectancyUpdate = true;
        }
        if (req.gender() != null) {
            entity.setGender(req.gender());
            needsLifeExpectancyUpdate = true;
        }
        if (req.retirementAge() != null) {
            entity.setRetirementAge(req.retirementAge());
            needsLifeExpectancyUpdate = true;
        }
        if (req.marriageYear() != null) entity.setMarriageYear(req.marriageYear());
        if (req.careerInsuranceType() != null) entity.setCareerInsuranceType(req.careerInsuranceType());
        if (req.biography() != null) entity.setBiography(req.biography());

        if (needsLifeExpectancyUpdate) {
            updateLifeExpectancy(entity);
        }

        ClientProfileEntity savedEntity = clientProfileRepository.save(entity);
        return clientProfileMapper.toProfileRes(savedEntity);
    }

    @Override
    @Transactional
    public PageResponse<ProfileRes> listClientProfilesByAgent(String agentUid, Pageable pageable) {
        Page<ClientProfileEntity> profilePage = clientProfileRepository.findAllByAgentFirebaseUid(agentUid, pageable);
        List<ClientProfileEntity> profiles = profilePage.getContent();
        profiles.forEach(this::bindClientFirebaseUid);
        List<ProfileRes> dtoList = profiles.stream()
                .map(clientProfileMapper::toProfileRes)
                .collect(Collectors.toList());
        return new PageResponse<>(dtoList, profilePage.getTotalElements(), profilePage.getNumber(), profilePage.getSize());
    }

    private ProfileRes createDefaultProfile(String uid) {
        ClientProfileEntity newProfile = new ClientProfileEntity();
        newProfile.setId(UUID.randomUUID());
        newProfile.setAgentFirebaseUid(uid);
        newProfile.setName("New Client");
        newProfile.setEmail(uid + "@default.com");
        newProfile.setPhone("");
        newProfile.setLineId("");
        newProfile.setRetirementAge(65);
        clientProfileRepository.save(newProfile);
        return clientProfileMapper.toProfileRes(newProfile);
    }

    private void updateLifeExpectancy(ClientProfileEntity entity) {
        if (entity.getBirthDate() == null || entity.getGender() == null) {
            return;
        }
        int currentAge = Period.between(entity.getBirthDate(), LocalDate.now()).getYears();
        entity.setCurrentAge(currentAge);
        int retirementAge = (entity.getRetirementAge() != null) ? entity.getRetirementAge() : 65;
        var currentLifeExp = metadataService.getLifeExpectancy(LocalDate.now().getYear(), entity.getGender(), currentAge);
        if (currentLifeExp != null) {
            entity.setLifeExpectancy(currentLifeExp.expectedLifespan().intValue());
        }
        var retirementLifeExp = metadataService.getLifeExpectancy(LocalDate.now().getYear(), entity.getGender(), retirementAge);
        if (retirementLifeExp != null) {
            entity.setLifeExpectancyAtRetirement(retirementLifeExp.expectedLifespan().intValue());
        }
    }

    private void bindClientFirebaseUid(ClientProfileEntity entity) {
        if (entity.getClientFirebaseUid() == null && entity.getEmail() != null) {
            try {
                UserRecord userRecord = firebaseAuth.getUserByEmail(entity.getEmail());
                entity.setClientFirebaseUid(userRecord.getUid());
                clientProfileRepository.save(entity);
                log.info("✅ Auto-bound Firebase UID {} to client profile for email {}", userRecord.getUid(), entity.getEmail());
            } catch (FirebaseAuthException e) {
                log.info("ℹ️ No existing Firebase user found for email {} during auto-binding. Skipping.", entity.getEmail());
            }
        }
    }
}
