package rocks.ifa.spring.domain.clientLaborPension;

public interface ClientLaborPensionService {
    void updateLaborPension(String uid, ClientLaborPensionContracts.UpdateLaborPensionReq req);
    ClientLaborPensionContracts.LaborPensionRes getLaborPension(String uid);
}
