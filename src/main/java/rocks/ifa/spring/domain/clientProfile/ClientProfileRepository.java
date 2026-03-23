package rocks.ifa.spring.domain.clientProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientProfileRepository extends JpaRepository<ClientProfileEntity, Long> {
    Optional<ClientProfileEntity> findByFirebaseUid(String firebaseUid);
    boolean existsByFirebaseUid(String firebaseUid);
    void deleteByFirebaseUid(String firebaseUid);
}
