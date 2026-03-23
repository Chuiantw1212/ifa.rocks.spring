package rocks.ifa.spring.domain.clientRetirement;

public interface ClientRetirementService {
    ClientRetirementContracts.RetirementRes getRetirement(String uid);
}
