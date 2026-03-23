package rocks.ifa.spring.domain.clientProfile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
    public ClientProfileDto getProfile(String uid) {
        return clientProfileRepository.findByFirebaseUid(uid)
                .map(this::convertToDto)
                .orElseGet(() -> createDefaultProfile(uid));
    }

    @Override
    @Transactional
    public void updateProfile(String uid, ClientProfileUpdateReq req) {
        ClientProfile entity = clientProfileRepository.findByFirebaseUid(uid)
                .orElseGet(() -> {
                    log.info("No existing profile for update, creating new one for UID: {}", uid);
                    ClientProfile newProfile = new ClientProfile();
                    newProfile.setFirebaseUid(uid);
                    return newProfile;
                });

        BeanUtils.copyProperties(req, entity);

        if (req.getBirthDate() != null) {
            entity.setCurrentAge(Period.between(req.getBirthDate(), LocalDate.now()).getYears());
        }

        clientProfileRepository.save(entity);
        log.info("✅ [Profile] Updated for user: {}", uid);
    }

    private ClientProfileDto createDefaultProfile(String uid) {
        ClientProfile newProfile = new ClientProfile();
        newProfile.setFirebaseUid(uid);
        clientProfileRepository.save(newProfile);
        log.info("✅ Minimal default profile created for UID: {}", uid);
        return convertToDto(newProfile);
    }

    private ClientProfileDto convertToDto(ClientProfile entity) {
        ClientProfileDto dto = new ClientProfileDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
