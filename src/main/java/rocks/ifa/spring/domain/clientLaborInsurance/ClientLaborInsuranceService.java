package rocks.ifa.spring.domain.clientLaborInsurance;

import rocks.ifa.spring.domain.clientLaborInsurance.contracts.LaborInsuranceRes;
import rocks.ifa.spring.domain.clientLaborInsurance.contracts.UpdateLaborInsuranceReq;

public interface ClientLaborInsuranceService {
    void updateLaborInsurance(String uid, UpdateLaborInsuranceReq req);
    LaborInsuranceRes getLaborInsurance(String uid);
}
