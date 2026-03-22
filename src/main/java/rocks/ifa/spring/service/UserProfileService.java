package rocks.ifa.spring.service;

import rocks.ifa.spring.model.UserProfileDto;
import rocks.ifa.spring.model.UserProfileUpdateReq;

public interface UserProfileService {
    UserProfileDto getProfile(String uid);
    void updateProfile(String uid, UserProfileUpdateReq req);
}
