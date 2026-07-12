package rocks.ifa.spring.domain.clientCreditCards;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClientCreditCardRepository extends JpaRepository<ClientCreditCardEntity, UUID> {
    /**
     * 根據客戶ID查找所有關聯的信用卡。
     * @param clientId 客戶的唯一識別碼 (UUID)
     * @return 信用卡實體列表
     */
    List<ClientCreditCardEntity> findByClientId(UUID clientId);
}
