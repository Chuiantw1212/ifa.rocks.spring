package rocks.ifa.spring.domain.clientCreditCards;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * 信用卡 (Credit Card)
 * 用於支出管理與現金流追蹤。
 * 這是一個獨立的實體，與客戶是一對多關係。
 */
@Getter
@Setter
@Entity
@Table(name = "client_credit_cards", indexes = {
    @Index(name = "idx_credit_card_client_id", columnList = "clientId")
})
public class ClientCreditCardEntity {

    /**
     * 信用卡自身的唯一主鍵
     */
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    /**
     * 關聯的客戶 ID (外鍵)
     */
    @Column(nullable = false)
    private UUID clientId;

    /**
     * 建立或最後修改此紀錄的顧問的 Firebase UID
     */
    @Column(name = "agent_firebase_uid", nullable = false)
    private String agentFirebaseUid;

    /**
     * 卡片名稱 (e.g. 玉山 U Bear)
     */
    @Column(nullable = false)
    private String name;

    /**
     * 扣款帳戶 (e.g. 台新 Richart)
     */
    private String deductionAccount;

    /**
     * 用途分類 (e.g. online, daily)
     */
    private String usageType;

    /**
     * 存放位置: 錢包(wallet) / 數位(digital) / 抽屜(drawer)
     */
    private String storageLocation;

    /**
     * 平均月刷卡金額
     */
    @Column(precision = 19, scale = 4)
    private BigDecimal averageMonthlyExpense;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
