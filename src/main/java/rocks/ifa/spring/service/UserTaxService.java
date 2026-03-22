package rocks.ifa.spring.service;

import rocks.ifa.spring.model.UserTaxDto;
import rocks.ifa.spring.model.UserTaxUpdateReq;

public interface UserTaxService {
    UserTaxDto getTax(String uid);
    void updateTax(String uid, UserTaxUpdateReq req);
}
