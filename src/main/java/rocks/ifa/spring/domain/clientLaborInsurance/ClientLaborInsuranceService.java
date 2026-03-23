package rocks.ifa.spring.domain.clientLaborInsurance;

public interface ClientLaborInsuranceService {
    void updateLaborInsurance(String uid, UserLaborInsuranceUpdateReq req);
    UserLaborInsuranceRes getLaborInsurance(String uid);
}
