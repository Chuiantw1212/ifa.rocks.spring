package rocks.ifa.spring.application.clientTax;

import rocks.ifa.spring.application.clientTax.dtos.TaxRes;
import rocks.ifa.spring.application.clientTax.dtos.UpdateTaxReq;

public interface ClientTaxApplicationService {
    void updateTax(String uid, UpdateTaxReq req);
    TaxRes getTax(String uid);
}
