package rocks.ifa.spring.domain.clientTax;

import org.springframework.stereotype.Service;

@Service
public class ClientTaxServiceImpl implements ClientTaxService {
    @Override
    public void updateTax(String uid, ClientTaxUpdateReq req) {
        // This is a placeholder. You should implement your own logic here.
    }

    @Override
    public ClientTaxRes getTax(String uid) {
        // This is a placeholder. You should implement your own logic here.
        return new ClientTaxRes();
    }
}
