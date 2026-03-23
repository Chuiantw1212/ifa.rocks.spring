package rocks.ifa.spring.domain.clientRetirement;

import org.springframework.stereotype.Service;

@Service
public class ClientRetirementServiceImpl implements ClientRetirementService {
    @Override
    public UserRetirementRes getRetirement(String uid) {
        // This is a placeholder. You should implement your own logic here.
        return new UserRetirementRes();
    }
}
