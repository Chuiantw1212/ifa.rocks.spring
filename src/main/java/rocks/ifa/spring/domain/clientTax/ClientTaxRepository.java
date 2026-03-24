package rocks.ifa.spring.domain.clientTax;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientTaxRepository extends JpaRepository<ClientTaxEntity, UUID> {
    Optional<ClientTaxEntity> findByAgentFirebaseUid(String agentFirebaseUid);
}
