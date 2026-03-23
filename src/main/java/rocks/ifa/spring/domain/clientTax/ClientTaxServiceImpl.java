package rocks.ifa.spring.domain.clientTax;

import org.springframework.stereotype.Service;

@Service
public class ClientTaxServiceImpl implements ClientTaxService {

    @Override
    public void updateTax(String uid, ClientTaxContracts.UpdateTaxReq req) {
        // Placeholder
    }

    @Override
    public ClientTaxContracts.TaxRes getTax(String uid) {
        // Placeholder
        return new ClientTaxContracts.TaxRes(null);
    }
}
