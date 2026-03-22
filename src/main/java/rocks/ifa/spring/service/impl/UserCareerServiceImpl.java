package rocks.ifa.spring.service.impl;

import org.springframework.stereotype.Service;
import rocks.ifa.spring.model.dto.UserCareerRes;
import rocks.ifa.spring.model.dto.UserCareerUpdateReq;
import rocks.ifa.spring.service.UserCareerService;

@Service
public class UserCareerServiceImpl implements UserCareerService {
    @Override
    public void updateCareer(String uid, UserCareerUpdateReq req) {
        // This is a placeholder. You should implement your own logic here.
    }

    @Override
    public UserCareerRes getCareer(String uid) {
        // This is a placeholder. You should implement your own logic here.
        return new UserCareerRes();
    }
}
