package rocks.ifa.spring.domain.clientCareer;

import org.springframework.stereotype.Service;

@Service
public class ClientCareerServiceImpl implements ClientCareerService {
    @Override
    public void updateCareer(String uid, ClientCareerUpdateReq req) {
        // This is a placeholder. You should implement your own logic here.
    }

    @Override
    public ClientCareerRes getCareer(String uid) {
        // This is a placeholder. You should implement your own logic here.
        return new ClientCareerRes();
    }
}
