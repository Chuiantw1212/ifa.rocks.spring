package rocks.ifa.spring.domain.clientLaborInsurance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.domain.clientLaborInsurance.dtos.LaborInsuranceRes;
import rocks.ifa.spring.domain.clientLaborInsurance.dtos.UpdateLaborInsuranceReq;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientLaborInsuranceServiceImpl implements ClientLaborInsuranceService {

    private final ClientLaborInsuranceRepository clientLaborInsuranceRepository;

    @Override
    public LaborInsuranceRes getLaborInsurance(String uid) {
        return clientLaborInsuranceRepository.findByAgentFirebaseUid(uid)
                .map(this::convertToRes)
                .orElse(null); // Or create a default one if needed
    }

    @Override
    @Transactional
    public void updateLaborInsurance(String uid, UpdateLaborInsuranceReq req) {
        ClientLaborInsuranceEntity entity = clientLaborInsuranceRepository.findByAgentFirebaseUid(uid)
                .orElseGet(() -> {
                    log.info("No existing labor insurance record, creating new one for UID: {}", uid);
                    ClientLaborInsuranceEntity newEntity = new ClientLaborInsuranceEntity();
                    newEntity.setId(UUID.randomUUID());
                    newEntity.setAgentFirebaseUid(uid);
                    return newEntity;
                });

        // Map all fields from request to entity
        entity.setExpectedClaimAge(req.expectedClaimAge());
        entity.setAverageMonthlySalary(req.averageMonthlySalary());
        entity.setInsuranceSeniority(req.insuranceSeniority());
        entity.setPredictedRemainingLife(req.predictedRemainingLife());
        entity.setPredictedMonthlyAnnuity(req.predictedMonthlyAnnuity());

        clientLaborInsuranceRepository.save(entity);
        log.info("✅ [LaborInsurance] Updated for user: {}", uid);
    }

    private LaborInsuranceRes convertToRes(ClientLaborInsuranceEntity entity) {
        return new LaborInsuranceRes(
            entity.getExpectedClaimAge(),
            entity.getAverageMonthlySalary(),
            entity.getInsuranceSeniority(),
            entity.getPredictedRemainingLife(),
            entity.getPredictedMonthlyAnnuity()
        );
    }
}
