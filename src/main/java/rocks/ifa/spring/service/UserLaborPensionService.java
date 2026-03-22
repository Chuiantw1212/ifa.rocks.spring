package rocks.ifa.spring.service;

import rocks.ifa.spring.model.UserLaborPensionDto;
import rocks.ifa.spring.model.UserLaborPensionUpdateReq;

public interface UserLaborPensionService {
    UserLaborPensionDto getLaborPension(String uid);
    void updateLaborPension(String uid, UserLaborPensionUpdateReq req);
}
