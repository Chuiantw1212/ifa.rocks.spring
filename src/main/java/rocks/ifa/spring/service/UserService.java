package rocks.ifa.spring.service;

import rocks.ifa.spring.model.dto.UserFullDataRes;

public interface UserService {
    UserFullDataRes getFullUserData(String uid);
}
