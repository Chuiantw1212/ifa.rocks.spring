package rocks.ifa.spring.application.clientLaborInsurance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rocks.ifa.spring.domain.clientLaborInsurance.ClientLaborInsuranceEntity;
import rocks.ifa.spring.domain.clientLaborInsurance.ClientLaborInsuranceRepository;
import rocks.ifa.spring.application.clientLaborInsurance.dtos.LaborInsuranceRes;
import rocks.ifa.spring.application.clientLaborInsurance.dtos.UpdateLaborInsuranceReq;
import rocks.ifa.spring.domain.clientProfile.ClientProfileRepository;
import rocks.ifa.spring.infrastructure.security.SecurityUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientLaborInsuranceServiceImpl implements ClientLaborInsuranceService {

    private final ClientLaborInsuranceRepository clientLaborInsuranceRepository;
    private final ClientProfileRepository clientProfileRepository;

    @Override
    public Optional<LaborInsuranceRes> getLaborInsurance(UUID clientId, String requesterUid) {
        authorizeAccess(clientId, requesterUid);
        return clientLaborInsuranceRepository.findById(clientId)
                .map(this::convertToRes);
    }

    @Override
    @Transactional
    public void updateLaborInsurance(UUID clientId, UpdateLaborInsuranceReq req, String requesterUid) {
        authorizeAccess(clientId, requesterUid);
        
        ClientLaborInsuranceEntity entity = clientLaborInsuranceRepository.findById(clientId)
                .orElseGet(() -> {
                    log.info("No existing labor insurance record for client ID: {}, creating new one.", clientId);
                    ClientLaborInsuranceEntity newEntity = new ClientLaborInsuranceEntity();
                    newEntity.setId(clientId);
                    newEntity.setAgentFirebaseUid(SecurityUtils.getCurrentUserUid());
                    return newEntity;
                });

        entity.setExpectedClaimAge(req.expectedClaimAge());
        entity.setAverageMonthlySalary(req.averageMonthlySalary());
        entity.setInsuranceSeniority(req.insuranceSeniority());
        entity.setPredictedRemainingLife(req.predictedRemainingLife());

        clientLaborInsuranceRepository.save(entity);
        log.info("✅ [LaborInsurance] Updated for client ID: {}", clientId);
    }

    private void authorizeAccess(UUID clientId, String requesterUid) {
        var profile = clientProfileRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Associated client profile not found."));
        
        boolean isOwnerAgent = Objects.equals(requesterUid, profile.getAgentFirebaseUid());
        boolean isClientSelf = Objects.equals(requesterUid, profile.getClientFirebaseUid());

        if (!isOwnerAgent && !isClientSelf) {
            log.warn("Unauthorized attempt to access labor insurance data for client {}. Requester UID: {}", clientId, requesterUid);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this data.");
        }
    }

    private LaborInsuranceRes convertToRes(ClientLaborInsuranceEntity entity) {
        if (entity == null) {
            return null;
        }
        return new LaborInsuranceRes(
            entity.getId(),
            entity.getExpectedClaimAge(),
            entity.getAverageMonthlySalary(),
            entity.getInsuranceSeniority(),
            entity.getPredictedRemainingLife(),
            entity.getPredictedMonthlyAnnuity()
        );
    }
}
