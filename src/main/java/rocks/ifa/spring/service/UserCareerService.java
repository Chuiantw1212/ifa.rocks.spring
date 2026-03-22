package rocks.ifa.spring.service;

import rocks.ifa.spring.model.UserCareerDto;
import rocks.ifa.spring.model.UserCareerUpdateReq;

public interface UserCareerService {
    UserCareerDto getCareer(String uid);
    void updateCareer(String uid, UserCareerUpdateReq req);
}
