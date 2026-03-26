package rocks.ifa.spring.domain.clientTax;

import rocks.ifa.spring.domain.clientTax.contracts.TaxRes;
import rocks.ifa.spring.domain.clientTax.contracts.UpdateTaxReq;

public interface ClientTaxService {
    void updateTax(String uid, UpdateTaxReq req);
    TaxRes getTax(String uid);
}
