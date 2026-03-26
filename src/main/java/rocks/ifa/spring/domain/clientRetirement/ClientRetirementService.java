package rocks.ifa.spring.domain.clientRetirement;

import rocks.ifa.spring.domain.clientRetirement.contracts.RetirementRes;

public interface ClientRetirementService {
    RetirementRes getRetirement(String uid);
}
