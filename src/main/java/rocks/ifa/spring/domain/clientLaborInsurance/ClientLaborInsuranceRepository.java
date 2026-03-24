package rocks.ifa.spring.domain.clientLaborInsurance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientLaborInsuranceRepository extends JpaRepository<ClientLaborInsuranceEntity, UUID> {
    Optional<ClientLaborInsuranceEntity> findByAgentFirebaseUid(String agentFirebaseUid);
}
