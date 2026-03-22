package rocks.ifa.spring.service;

import rocks.ifa.spring.model.dto.UserTaxRes;
import rocks.ifa.spring.model.dto.UserTaxUpdateReq;

public interface UserTaxService {
    void updateTax(String uid, UserTaxUpdateReq req);
    UserTaxRes getTax(String uid);
}
