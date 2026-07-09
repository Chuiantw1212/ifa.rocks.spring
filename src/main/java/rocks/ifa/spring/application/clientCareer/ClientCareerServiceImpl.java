package rocks.ifa.spring.application.clientCareer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rocks.ifa.spring.application.clientCareer.dtos.CareerRes;
import rocks.ifa.spring.application.clientCareer.dtos.UpdateCareerReq;
import rocks.ifa.spring.domain.clientCareer.ClientCareerEntity;
import rocks.ifa.spring.domain.clientCareer.ClientCareerRepository;
import rocks.ifa.spring.domain.clientProfile.ClientProfileRepository;
import rocks.ifa.spring.infrastructure.security.SecurityUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientCareerServiceImpl implements ClientCareerService {

    private final ClientCareerRepository clientCareerRepository;
    private final ClientProfileRepository clientProfileRepository;

    @Override
    public Optional<CareerRes> getCareer(UUID clientId, String requesterUid) {
        authorizeAccess(clientId, requesterUid);
        return clientCareerRepository.findById(clientId)
                .map(this::convertToRes); // No more orElse(null)
    }

    @Override
    @Transactional
    public void updateCareer(UUID clientId, UpdateCareerReq req, String requesterUid) {
        authorizeAccess(clientId, requesterUid);
        
        ClientCareerEntity entity = clientCareerRepository.findById(clientId)
                .orElseGet(() -> {
                    log.info("No existing career record for client ID: {}, creating new one.", clientId);
                    ClientCareerEntity newEntity = new ClientCareerEntity();
                    newEntity.setId(clientId);
                    newEntity.setAgentFirebaseUid(SecurityUtils.getCurrentUserUid());
                    return newEntity;
                });

        // Update all fields from the request
        entity.setBaseSalary(req.baseSalary());
        entity.setOtherAllowance(req.otherAllowance());
        entity.setAnnualBonus(req.annualBonus());
        entity.setLaborInsurance(req.laborInsurance());
        entity.setHealthInsurance(req.healthInsurance());
        entity.setOtherDeduction(req.otherDeduction());
        entity.setPensionPersonalRate(req.pensionPersonalRate());
        entity.setPensionPersonalAmount(req.pensionPersonalAmount());
        entity.setPensionEmployerAmount(req.pensionEmployerAmount());
        entity.setPensionTotalAmount(req.pensionTotalAmount());
        entity.setStockDeduction(req.stockDeduction());
        entity.setStockCompanyMatch(req.stockCompanyMatch());
        entity.setMonthlyNetIncome(req.monthlyNetIncome());
        entity.setAnnualTotalIncome(req.annualTotalIncome());
        entity.setDependents(req.dependents());

        clientCareerRepository.save(entity);
        log.info("✅ [Career] Updated for client ID: {}", clientId);
    }

    private void authorizeAccess(UUID clientId, String requesterUid) {
        var profile = clientProfileRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Associated client profile not found."));
        
        boolean isOwnerAgent = Objects.equals(requesterUid, profile.getAgentFirebaseUid());
        boolean isClientSelf = Objects.equals(requesterUid, profile.getClientFirebaseUid());

        if (!isOwnerAgent && !isClientSelf) {
            log.warn("Unauthorized attempt to access career data for client {}. Requester UID: {}", clientId, requesterUid);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this data.");
        }
    }

    private CareerRes convertToRes(ClientCareerEntity entity) {
        if (entity == null) {
            return null;
        }
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
