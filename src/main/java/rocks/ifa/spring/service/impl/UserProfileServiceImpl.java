package rocks.ifa.spring.service.impl;

import org.springframework.stereotype.Service;
import rocks.ifa.spring.model.dto.UserProfileRes;
import rocks.ifa.spring.model.dto.UserProfileUpdateReq;
import rocks.ifa.spring.service.UserProfileService;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    @Override
    public void updateProfile(String uid, UserProfileUpdateReq req) {
        // This is a placeholder. You should implement your own logic here.
    }

    @Override
    public UserProfileRes getProfile(String uid) {
        // This is a placeholder. You should implement your own logic here.
        return new UserProfileRes();
    }
}
