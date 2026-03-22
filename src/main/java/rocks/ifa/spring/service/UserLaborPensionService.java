package rocks.ifa.spring.service;

import rocks.ifa.spring.model.dto.UserLaborPensionRes;
import rocks.ifa.spring.model.dto.UserLaborPensionUpdateReq;

public interface UserLaborPensionService {
    void updateLaborPension(String uid, UserLaborPensionUpdateReq req);
    UserLaborPensionRes getLaborPension(String uid);
}
