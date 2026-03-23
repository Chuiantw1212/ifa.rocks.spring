package rocks.ifa.spring.domain.clientLaborInsurance;

public interface ClientLaborInsuranceService {
    void updateLaborInsurance(String uid, ClientLaborInsuranceUpdateReq req);
    ClientLaborInsuranceRes getLaborInsurance(String uid);
}
