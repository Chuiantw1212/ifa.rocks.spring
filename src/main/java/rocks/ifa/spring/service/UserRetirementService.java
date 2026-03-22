package rocks.ifa.spring.service;

import rocks.ifa.spring.model.UserRetirementDto;
import rocks.ifa.spring.model.UserRetirementUpdateReq;

public interface UserRetirementService {
    UserRetirementDto getRetirement(String uid);
    void updateRetirement(String uid, UserRetirementUpdateReq req);
    void patchRetirement(String uid, UserRetirementDto req);
}
