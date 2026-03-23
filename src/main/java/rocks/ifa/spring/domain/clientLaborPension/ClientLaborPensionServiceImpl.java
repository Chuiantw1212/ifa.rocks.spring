package rocks.ifa.spring.domain.clientLaborPension;

import org.springframework.stereotype.Service;

@Service
public class ClientLaborPensionServiceImpl implements ClientLaborPensionService {

    @Override
    public void updateLaborPension(String uid, ClientLaborPensionContracts.UpdateLaborPensionReq req) {
        // Placeholder
    }

    @Override
    public ClientLaborPensionContracts.LaborPensionRes getLaborPension(String uid) {
        // Placeholder
        return new ClientLaborPensionContracts.LaborPensionRes(null, null, null, null, null, null, null, null, null, null);
    }
}
