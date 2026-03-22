package rocks.ifa.spring.service.impl;

import org.springframework.stereotype.Service;
import rocks.ifa.spring.model.dto.UserLaborInsuranceRes;
import rocks.ifa.spring.model.dto.UserLaborInsuranceUpdateReq;
import rocks.ifa.spring.service.UserLaborInsuranceService;

@Service
public class UserLaborInsuranceServiceImpl implements UserLaborInsuranceService {
    @Override
    public void updateLaborInsurance(String uid, UserLaborInsuranceUpdateReq req) {
        // This is a placeholder. You should implement your own logic here.
    }

    @Override
    public UserLaborInsuranceRes getLaborInsurance(String uid) {
        // This is a placeholder. You should implement your own logic here.
        return new UserLaborInsuranceRes();
    }
}
