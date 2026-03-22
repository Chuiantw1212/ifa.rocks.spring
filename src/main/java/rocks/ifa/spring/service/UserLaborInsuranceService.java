package rocks.ifa.spring.service;

import rocks.ifa.spring.model.dto.UserLaborInsuranceRes;
import rocks.ifa.spring.model.dto.UserLaborInsuranceUpdateReq;

public interface UserLaborInsuranceService {
    void updateLaborInsurance(String uid, UserLaborInsuranceUpdateReq req);
    UserLaborInsuranceRes getLaborInsurance(String uid);
}
