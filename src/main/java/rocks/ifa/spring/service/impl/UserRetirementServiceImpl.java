package rocks.ifa.spring.service.impl;

import org.springframework.stereotype.Service;
import rocks.ifa.spring.model.dto.UserRetirementRes;
import rocks.ifa.spring.service.UserRetirementService;

@Service
public class UserRetirementServiceImpl implements UserRetirementService {
    @Override
    public UserRetirementRes getRetirement(String uid) {
        // This is a placeholder. You should implement your own logic here.
        return new UserRetirementRes();
    }
}
