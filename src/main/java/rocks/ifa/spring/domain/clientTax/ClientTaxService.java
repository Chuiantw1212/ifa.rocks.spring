package rocks.ifa.spring.domain.clientTax;

public interface ClientTaxService {
    void updateTax(String uid, ClientTaxUpdateReq req);
    ClientTaxRes getTax(String uid);
}
