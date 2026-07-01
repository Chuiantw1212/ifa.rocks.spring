package rocks.ifa.spring.domain.clientLaborPension;

import rocks.ifa.spring.domain.clientLaborPension.dtos.LaborPensionRes;
import rocks.ifa.spring.domain.clientLaborPension.dtos.UpdateLaborPensionReq;

import java.util.Optional;
import java.util.UUID;

public interface ClientLaborPensionService {
    Optional<LaborPensionRes> getLaborPension(UUID clientId, String requesterUid);
    void updateLaborPension(UUID clientId, UpdateLaborPensionReq req, String requesterUid);
}
