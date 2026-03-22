package rocks.ifa.spring.service;

import rocks.ifa.spring.model.dto.UserFullDataRes;
import rocks.ifa.spring.model.dto.UserRegistrationReq;

public interface UserService {
    UserFullDataRes getFullUserData(String uid);
    void deleteUser(String uid);
    void registerUser(UserRegistrationReq req);
}
