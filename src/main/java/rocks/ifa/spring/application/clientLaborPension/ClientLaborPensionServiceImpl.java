package rocks.ifa.spring.application.clientLaborPension;

import com.alibaba.cola.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.application.clientLaborPension.dtos.LaborPensionRes;
import rocks.ifa.spring.application.clientLaborPension.dtos.UpdateLaborPensionReq;
import rocks.ifa.spring.domain.clientLaborPension.ClientLaborPensionEntity;
import rocks.ifa.spring.domain.clientLaborPension.ClientLaborPensionRepository;
import rocks.ifa.spring.domain.clientProfile.ClientProfileRepository;
import rocks.ifa.spring.infrastructure.config.SecurityUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientLaborPensionServiceImpl implements ClientLaborPensionService {

    private final ClientLaborPensionRepository clientLaborPensionRepository;
    private final ClientProfileRepository clientProfileRepository;

    @Override
    public Optional<LaborPensionRes> getLaborPension(UUID clientId, String requesterUid) {
        authorizeAccess(clientId, requesterUid);
        return clientLaborPensionRepository.findById(clientId)
                .map(this::convertToRes);
    }

    @Override
    @Transactional
    public void updateLaborPension(UUID clientId, UpdateLaborPensionReq req, String requesterUid) {
        authorizeAccess(clientId, requesterUid);
        
        ClientLaborPensionEntity entity = clientLaborPensionRepository.findById(clientId)
                .orElseGet(() -> {
                    log.info("No existing labor pension record for client ID: {}, creating new one.", clientId);
                    ClientLaborPensionEntity newEntity = new ClientLaborPensionEntity();
                    newEntity.setId(clientId);
                    newEntity.setAgentFirebaseUid(SecurityUtils.getCurrentUserUid());
                    return newEntity;
                });

        entity.setExpectedRetirementAge(req.expectedRetirementAge());
        entity.setRemainingLifeAtRetirement(req.remainingLifeAtRetirement());
        entity.setRetirementRoi(req.retirementRoi());
        entity.setEmployerContribution(req.employerContribution());
        entity.setEmployerEarnings(req.employerEarnings());
        entity.setPersonalContribution(req.personalContribution());
        entity.setPersonalEarnings(req.personalEarnings());
        entity.setCurrentWorkSeniority(req.currentWorkSeniority());

        clientLaborPensionRepository.save(entity);
        log.info("✅ [LaborPension] Updated for client ID: {}", clientId);
    }

    private void authorizeAccess(UUID clientId, String requesterUid) {
        var profile = clientProfileRepository.findById(clientId)
                .orElseThrow(() -> new BizException("CLIENT_NOT_FOUND", "Associated client profile not found."));
        
        boolean isOwnerAgent = Objects.equals(requesterUid, profile.getAgentFirebaseUid());
        boolean isClientSelf = Objects.equals(requesterUid, profile.getClientFirebaseUid());

        if (!isOwnerAgent && !isClientSelf) {
            log.warn("Unauthorized attempt to access labor pension data for client {}. Requester UID: {}", clientId, requesterUid);
            throw new BizException("FORBIDDEN", "You do not have permission to access this data.");
        }
    }

    private LaborPensionRes convertToRes(ClientLaborPensionEntity entity) {
        if (entity == null) {
            return null;
        }
        return new LaborPensionRes(
            entity.getId(),
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
