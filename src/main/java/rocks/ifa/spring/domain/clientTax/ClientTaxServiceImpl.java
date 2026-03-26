package rocks.ifa.spring.domain.clientTax;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.ifa.spring.domain.clientTax.contracts.TaxRes;
import rocks.ifa.spring.domain.clientTax.contracts.UpdateTaxReq;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientTaxServiceImpl implements ClientTaxService {

    private final ClientTaxRepository clientTaxRepository;

    @Override
    public TaxRes getTax(String uid) {
        return clientTaxRepository.findByAgentFirebaseUid(uid)
                .map(this::convertToRes)
                .orElse(null); // Or create a default one if needed
    }

    @Override
    @Transactional
    public void updateTax(String uid, UpdateTaxReq req) {
        ClientTaxEntity entity = clientTaxRepository.findByAgentFirebaseUid(uid)
                .orElseGet(() -> {
                    log.info("No existing tax record, creating new one for UID: {}", uid);
                    ClientTaxEntity newEntity = new ClientTaxEntity();
                    newEntity.setId(UUID.randomUUID());
                    newEntity.setAgentFirebaseUid(uid);
                    return newEntity;
                });

        entity.setEstimatedOtherIncome(req.estimatedOtherIncome());

        clientTaxRepository.save(entity);
        log.info("✅ [Tax] Updated for user: {}", uid);
    }

    private TaxRes convertToRes(ClientTaxEntity entity) {
        return new TaxRes(entity.getEstimatedOtherIncome());
    }
}
