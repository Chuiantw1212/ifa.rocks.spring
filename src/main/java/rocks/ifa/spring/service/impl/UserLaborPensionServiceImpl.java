package rocks.ifa.spring.service.impl;

import org.springframework.stereotype.Service;
import rocks.ifa.spring.model.dto.UserLaborPensionRes;
import rocks.ifa.spring.model.dto.UserLaborPensionUpdateReq;
import rocks.ifa.spring.service.UserLaborPensionService;

@Service
public class UserLaborPensionServiceImpl implements UserLaborPensionService {
    @Override
    public void updateLaborPension(String uid, UserLaborPensionUpdateReq req) {
        // This is a placeholder. You should implement your own logic here.
    }

    @Override
    public UserLaborPensionRes getLaborPension(String uid) {
        // This is a placeholder. You should implement your own logic here.
        return new UserLaborPensionRes();
    }
}
