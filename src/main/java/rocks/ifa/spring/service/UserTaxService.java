package rocks.ifa.spring.service;

import rocks.ifa.spring.dto.UserTaxDto;
import rocks.ifa.spring.dto.UserTaxUpdateReq;

public interface UserTaxService {
    UserTaxDto getTax(String uid);
    void updateTax(String uid, UserTaxUpdateReq req);
}
