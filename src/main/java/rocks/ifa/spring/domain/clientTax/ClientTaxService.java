package rocks.ifa.spring.domain.clientTax;

public interface ClientTaxService {
    void updateTax(String uid, UserTaxUpdateReq req);
    UserTaxRes getTax(String uid);
}
