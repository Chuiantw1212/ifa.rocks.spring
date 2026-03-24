package rocks.ifa.spring.domain.clientTax;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientTaxRepository extends JpaRepository<ClientTaxEntity, Long> {
    Optional<ClientTaxEntity> findByAgentFirebaseUid(String agentFirebaseUid);
}
