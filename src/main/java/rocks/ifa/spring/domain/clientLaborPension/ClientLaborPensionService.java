package rocks.ifa.spring.domain.clientLaborPension;

import rocks.ifa.spring.domain.clientLaborPension.contracts.LaborPensionRes;
import rocks.ifa.spring.domain.clientLaborPension.contracts.UpdateLaborPensionReq;

public interface ClientLaborPensionService {
    void updateLaborPension(String uid, UpdateLaborPensionReq req);
    LaborPensionRes getLaborPension(String uid);
}
