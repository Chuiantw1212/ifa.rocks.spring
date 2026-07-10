package rocks.ifa.spring.application.clientRetirement;

import rocks.ifa.spring.application.clientRetirement.dtos.RetirementRes;

public interface ClientRetirementApplicationService {
    RetirementRes getRetirement(String uid);
}
