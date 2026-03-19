package rocks.ifa.spring.service.impl;

import org.springframework.stereotype.Service;
import rocks.ifa.spring.model.dto.UserFullDataRes;
import rocks.ifa.spring.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserFullDataRes getFullUserData(String uid) {
        // This is a placeholder. You should implement your own logic here.
        return new UserFullDataRes();
    }
}
