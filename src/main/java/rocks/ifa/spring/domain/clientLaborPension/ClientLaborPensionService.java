package rocks.ifa.spring.domain.clientLaborPension;

public interface ClientLaborPensionService {
    void updateLaborPension(String uid, UserLaborPensionUpdateReq req);
    UserLaborPensionRes getLaborPension(String uid);
}
