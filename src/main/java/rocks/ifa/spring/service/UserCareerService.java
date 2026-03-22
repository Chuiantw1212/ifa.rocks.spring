package rocks.ifa.spring.service;

import rocks.ifa.spring.model.dto.UserCareerRes;
import rocks.ifa.spring.model.dto.UserCareerUpdateReq;

public interface UserCareerService {
    void updateCareer(String uid, UserCareerUpdateReq req);
    UserCareerRes getCareer(String uid);
}
