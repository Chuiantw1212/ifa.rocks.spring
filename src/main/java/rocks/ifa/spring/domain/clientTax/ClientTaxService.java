package rocks.ifa.spring.domain.clientTax;

import rocks.ifa.spring.domain.clientTax.dtos.TaxRes;
import rocks.ifa.spring.domain.clientTax.dtos.UpdateTaxReq;

public interface ClientTaxService {
    void updateTax(String uid, UpdateTaxReq req);
    TaxRes getTax(String uid);
}
