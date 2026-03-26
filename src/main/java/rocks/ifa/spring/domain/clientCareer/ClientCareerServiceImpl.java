package rocks.ifa.spring.domain.clientCareer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.domain.clientCareer.contracts.CareerRes;
import rocks.ifa.spring.domain.clientCareer.contracts.UpdateCareerReq;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientCareerServiceImpl implements ClientCareerService {

    private final ClientCareerRepository clientCareerRepository;

    @Override
    public CareerRes getCareer(String uid) {
        return clientCareerRepository.findByAgentFirebaseUid(uid)
                .map(this::convertToRes)
                .orElse(null); // Or create a default one if needed
    }

    @Override
    @Transactional
    public void updateCareer(String uid, UpdateCareerReq req) {
        ClientCareerEntity entity = clientCareerRepository.findByAgentFirebaseUid(uid)
                .orElseGet(() -> {
                    log.info("No existing career record, creating new one for UID: {}", uid);
                    ClientCareerEntity newEntity = new ClientCareerEntity();
                    newEntity.setId(UUID.randomUUID());
                    newEntity.setAgentFirebaseUid(uid);
                    return newEntity;
                });

        // Map all fields from request to entity
        entity.setBaseSalary(req.baseSalary());
        entity.setOtherAllowance(req.otherAllowance());
        entity.setLaborInsurance(req.laborInsurance());
        entity.setHealthInsurance(req.healthInsurance());
        entity.setOtherDeduction(req.otherDeduction());
        entity.setPensionPersonalRate(req.pensionPersonalRate());
        entity.setStockDeduction(req.stockDeduction());
        entity.setStockCompanyMatch(req.stockCompanyMatch());
        entity.setAnnualBonus(req.annualBonus());
        entity.setDependents(req.dependents());

        // Here you would calculate derived fields
        // e.g., entity.setPensionPersonalAmount(...)

        clientCareerRepository.save(entity);
        log.info("✅ [Career] Updated for user: {}", uid);
    }

    private CareerRes convertToRes(ClientCareerEntity entity) {
        return new CareerRes(
            entity.getBaseSalary(),
            entity.getOtherAllowance(),
            entity.getLaborInsurance(),
            entity.getHealthInsurance(),
            entity.getOtherDeduction(),
            entity.getPensionPersonalRate(),
            entity.getPensionPersonalAmount(),
            entity.getPensionEmployerAmount(),
            entity.getPensionTotalAmount(),
            entity.getStockDeduction(),
            entity.getStockCompanyMatch(),
            entity.getMonthlyNetIncome(),
            entity.getAnnualBonus(),
            entity.getAnnualTotalIncome(),
            entity.getDependents()
        );
    }
}
