package rocks.ifa.spring.service;

import rocks.ifa.spring.model.UserFullDataRes;

public interface UserService {
    UserFullDataRes getFullUserData(String uid);
    void syncUser(String uid);
    void deleteUser(String uid);
}
