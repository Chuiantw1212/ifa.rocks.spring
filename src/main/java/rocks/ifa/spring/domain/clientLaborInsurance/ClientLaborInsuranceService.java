package rocks.ifa.spring.domain.clientLaborInsurance;

import rocks.ifa.spring.domain.clientLaborInsurance.dtos.LaborInsuranceRes;
import rocks.ifa.spring.domain.clientLaborInsurance.dtos.UpdateLaborInsuranceReq;

public interface ClientLaborInsuranceService {
    void updateLaborInsurance(String uid, UpdateLaborInsuranceReq req);
    LaborInsuranceRes getLaborInsurance(String uid);
}
