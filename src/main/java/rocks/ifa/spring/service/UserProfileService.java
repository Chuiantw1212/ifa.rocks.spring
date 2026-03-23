package rocks.ifa.spring.service;

import rocks.ifa.spring.dto.UserProfileDto;
import rocks.ifa.spring.dto.UserProfileUpdateReq;

public interface UserProfileService {
    UserProfileDto getProfile(String uid);
    void updateProfile(String uid, UserProfileUpdateReq req);
}
