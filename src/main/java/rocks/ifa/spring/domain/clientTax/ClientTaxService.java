package rocks.ifa.spring.domain.clientTax;

public interface ClientTaxService {
    void updateTax(String uid, ClientTaxContracts.UpdateTaxReq req);
    ClientTaxContracts.TaxRes getTax(String uid);
}
