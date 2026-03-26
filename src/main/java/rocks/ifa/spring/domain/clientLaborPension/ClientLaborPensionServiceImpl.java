package rocks.ifa.spring.domain.clientLaborPension;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.domain.clientLaborPension.contracts.LaborPensionRes;
import rocks.ifa.spring.domain.clientLaborPension.contracts.UpdateLaborPensionReq;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientLaborPensionServiceImpl implements ClientLaborPensionService {

    private final ClientLaborPensionRepository clientLaborPensionRepository;

    @Override
    public LaborPensionRes getLaborPension(String uid) {
        return clientLaborPensionRepository.findByAgentFirebaseUid(uid)
                .map(this::convertToRes)
                .orElse(null); // Or create a default one if needed
    }

    @Override
    @Transactional
    public void updateLaborPension(String uid, UpdateLaborPensionReq req) {
        ClientLaborPensionEntity entity = clientLaborPensionRepository.findByAgentFirebaseUid(uid)
                .orElseGet(() -> {
                    log.info("No existing labor pension record, creating new one for UID: {}", uid);
                    ClientLaborPensionEntity newEntity = new ClientLaborPensionEntity();
                    newEntity.setId(UUID.randomUUID());
                    newEntity.setAgentFirebaseUid(uid);
                    return newEntity;
                });

        // Map fields from request to entity
        entity.setExpectedRetirementAge(req.expectedRetirementAge());
        entity.setRetirementRoi(req.retirementRoi());
        entity.setEmployerContribution(req.employerContribution());
        entity.setEmployerEarnings(req.employerEarnings());
        entity.setPersonalContribution(req.personalContribution());
        entity.setPersonalEarnings(req.personalEarnings());
        entity.setCurrentWorkSeniority(req.currentWorkSeniority());

        // Here you would typically calculate the derived fields
        // entity.setPredictedLumpSum(...);
        // entity.setPredictedNetLumpSum(...);

        clientLaborPensionRepository.save(entity);
        log.info("✅ [LaborPension] Updated for user: {}", uid);
    }

    private LaborPensionRes convertToRes(ClientLaborPensionEntity entity) {
        return new LaborPensionRes(
            entity.getExpectedRetirementAge(),
            entity.getRemainingLifeAtRetirement(),
            entity.getRetirementRoi(),
            entity.getEmployerContribution(),
            entity.getEmployerEarnings(),
            entity.getPersonalContribution(),
            entity.getPersonalEarnings(),
            entity.getCurrentWorkSeniority(),
            entity.getPredictedLumpSum(),
            entity.getPredictedNetLumpSum()
        );
    }
}
