package rocks.ifa.spring.application.clientLaborInsurance;

import rocks.ifa.spring.application.clientLaborInsurance.dtos.LaborInsuranceRes;
import rocks.ifa.spring.application.clientLaborInsurance.dtos.UpdateLaborInsuranceReq;

import java.util.Optional;
import java.util.UUID;

public interface ClientLaborInsuranceService {
    Optional<LaborInsuranceRes> getLaborInsurance(UUID clientId, String requesterUid);
    void updateLaborInsurance(UUID clientId, UpdateLaborInsuranceReq req, String requesterUid);
}
