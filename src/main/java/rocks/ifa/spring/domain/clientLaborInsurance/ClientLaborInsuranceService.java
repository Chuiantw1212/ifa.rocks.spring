package rocks.ifa.spring.domain.clientLaborInsurance;

public interface ClientLaborInsuranceService {
    void updateLaborInsurance(String uid, ClientLaborInsuranceContracts.UpdateLaborInsuranceReq req);
    ClientLaborInsuranceContracts.LaborInsuranceRes getLaborInsurance(String uid);
}
