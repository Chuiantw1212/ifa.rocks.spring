package rocks.ifa.spring.domain.clientLaborInsurance;

import rocks.ifa.spring.domain.clientLaborInsurance.dtos.LaborInsuranceRes;
import rocks.ifa.spring.domain.clientLaborInsurance.dtos.UpdateLaborInsuranceReq;

import java.util.UUID;

public interface ClientLaborInsuranceService {
    LaborInsuranceRes getLaborInsurance(UUID clientId, String requesterUid);
    void updateLaborInsurance(UUID clientId, UpdateLaborInsuranceReq req, String requesterUid);
}
