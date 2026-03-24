package rocks.ifa.spring.domain.clientLaborInsurance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientLaborInsuranceRepository extends JpaRepository<ClientLaborInsuranceEntity, Long> {
    Optional<ClientLaborInsuranceEntity> findByAgentFirebaseUid(String agentFirebaseUid);
}
