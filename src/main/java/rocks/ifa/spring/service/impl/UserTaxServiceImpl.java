package rocks.ifa.spring.service.impl;

import org.springframework.stereotype.Service;
import rocks.ifa.spring.model.dto.UserTaxRes;
import rocks.ifa.spring.model.dto.UserTaxUpdateReq;
import rocks.ifa.spring.service.UserTaxService;

@Service
public class UserTaxServiceImpl implements UserTaxService {
    @Override
    public void updateTax(String uid, UserTaxUpdateReq req) {
        // This is a placeholder. You should implement your own logic here.
    }

    @Override
    public UserTaxRes getTax(String uid) {
        // This is a placeholder. You should implement your own logic here.
        return new UserTaxRes();
    }
}
