package rocks.ifa.spring.domain.clientLaborPension;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientLaborPensionRepository extends JpaRepository<ClientLaborPensionEntity, UUID> {
}
