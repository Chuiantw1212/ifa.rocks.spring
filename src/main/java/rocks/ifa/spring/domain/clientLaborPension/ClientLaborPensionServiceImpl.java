package rocks.ifa.spring.domain.clientLaborPension;

import org.springframework.stereotype.Service;

@Service
public class ClientLaborPensionServiceImpl implements ClientLaborPensionService {
    @Override
    public void updateLaborPension(String uid, ClientLaborPensionUpdateReq req) {
        // This is a placeholder. You should implement your own logic here.
    }

    @Override
    public ClientLaborPensionRes getLaborPension(String uid) {
        // This is a placeholder. You should implement your own logic here.
        return new ClientLaborPensionRes();
    }
}
