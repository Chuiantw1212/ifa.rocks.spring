package rocks.ifa.spring.service;

import rocks.ifa.spring.model.dto.UserProfileRes;
import rocks.ifa.spring.model.dto.UserProfileUpdateReq;

public interface UserProfileService {
    void updateProfile(String uid, UserProfileUpdateReq req);
    UserProfileRes getProfile(String uid);
}
