package rocks.ifa.spring.domain.clientRetirement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rocks.ifa.spring.domain.clientRetirement.dtos.RetirementRes;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientRetirementServiceImpl implements ClientRetirementService {

    private final ClientRetirementRepository clientRetirementRepository;

    @Override
    public RetirementRes getRetirement(String uid) {
        return clientRetirementRepository.findByAgentFirebaseUid(uid)
                .map(this::convertToRes)
                .orElse(null); // Or create a default one if needed
    }

    private RetirementRes convertToRes(ClientRetirementEntity entity) {
        return new RetirementRes(
            entity.getId(),
            entity.getUpdatedAt(),
            entity.getHouseholdType(),
            entity.getHousingMode(),
            entity.getHousingCost(),
            entity.getHealthTierCode(),
            entity.getHealthCost(),
            entity.getActiveLivingCode(),
            entity.getActiveLivingCost(),
            entity.getSlowGoStartAge(),
            entity.getDefenseTierCode(),
            entity.getMonthlyMedicalCost(),
            entity.getCriticalIllnessCode(),
            entity.getCriticalIllnessReserve(),
            entity.getNogoStartAge(),
            entity.getLtcCareMode(),
            entity.getLtcMonthlyCost(),
            entity.getLtcMonthlySupplies(),
            entity.getLtcSubsidy()
        );
    }
}
