package rocks.ifa.spring.domain.clientRetirement;

import rocks.ifa.spring.domain.clientRetirement.dtos.RetirementRes;

public interface ClientRetirementService {
    RetirementRes getRetirement(String uid);
}
