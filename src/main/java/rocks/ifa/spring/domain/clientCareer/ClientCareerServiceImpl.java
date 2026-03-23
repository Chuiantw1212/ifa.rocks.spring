package rocks.ifa.spring.domain.clientCareer;

import org.springframework.stereotype.Service;

@Service
public class ClientCareerServiceImpl implements ClientCareerService {
    
    // In a real implementation, you would inject a repository here.
    // private final ClientCareerRepository clientCareerRepository;

    @Override
    public void updateCareer(String uid, ClientCareerContracts.UpdateCareerReq req) {
        // Placeholder: Find entity by UID, update it with req data, and save.
    }

    @Override
    public ClientCareerContracts.CareerRes getCareer(String uid) {
        // Placeholder: Find entity by UID and convert it to the CareerRes record.
        return new ClientCareerContracts.CareerRes(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
}
