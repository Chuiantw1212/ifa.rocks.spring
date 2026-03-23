package rocks.ifa.spring.domain.clientLaborPension;

public interface ClientLaborPensionService {
    void updateLaborPension(String uid, ClientLaborPensionUpdateReq req);
    ClientLaborPensionRes getLaborPension(String uid);
}
