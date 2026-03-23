package rocks.ifa.spring.domain.clientLaborInsurance;

import org.springframework.stereotype.Service;

@Service
public class ClientLaborInsuranceServiceImpl implements ClientLaborInsuranceService {

    @Override
    public void updateLaborInsurance(String uid, ClientLaborInsuranceContracts.UpdateLaborInsuranceReq req) {
        // Placeholder
    }

    @Override
    public ClientLaborInsuranceContracts.LaborInsuranceRes getLaborInsurance(String uid) {
        // Placeholder
        return new ClientLaborInsuranceContracts.LaborInsuranceRes(null, null, null, null, null);
    }
}
