package rocks.ifa.spring.domain.clientCareer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientCareerRepository extends JpaRepository<ClientCareerEntity, Long> {
    Optional<ClientCareerEntity> findByFirebaseUid(String firebaseUid);
}
