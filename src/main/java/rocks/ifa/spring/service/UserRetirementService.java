package rocks.ifa.spring.service;

import rocks.ifa.spring.model.dto.UserRetirementRes;

public interface UserRetirementService {
    UserRetirementRes getRetirement(String uid);
}
