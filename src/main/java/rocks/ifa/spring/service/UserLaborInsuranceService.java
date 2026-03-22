package rocks.ifa.spring.service;

import rocks.ifa.spring.model.UserLaborInsuranceDto;
import rocks.ifa.spring.model.UserLaborInsuranceUpdateReq;

public interface UserLaborInsuranceService {
    UserLaborInsuranceDto getLaborInsurance(String uid);
    void updateLaborInsurance(String uid, UserLaborInsuranceUpdateReq req);
}
