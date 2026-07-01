package rocks.ifa.spring.domain.entityBase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * 作為所有「客戶」子領域實體的抽象基底類別。
 * <p>
 * 這些子領域實體與核心的 {@code ClientProfileEntity} 之間，共享一種一對一的關聯關係。
 * 此類別提供了通用的欄位，例如主鍵 (`id`)、建立此紀錄的顧問識別碼 (`agentFirebaseUid`) 以及時間戳。
 *
 * <h2>共享主鍵策略 (Shared Primary Key Strategy)</h2>
 * 本實體模型採用「共享主鍵」策略來處理一對一關聯。
 * 這意味著，子類別實體 (例如 {@code ClientCareerEntity}) 的主鍵 (`id`)，同時也是一個指向
 * 父實體 {@code ClientProfileEntity} 主鍵的外鍵。
 *
 * <h2>資料庫欄位命名慣例</h2>
 * 雖然在 Java 程式碼中，主鍵的欄位名統一為 `id`，但在資料庫中，對應的欄位名稱應該反映其業務含義。
 * 我們透過在子類別上使用 {@link AttributeOverride} 註解來實現這一點。
 *
 * <p><b>在子類別中的使用範例:</b></p>
 * <pre>{@code
 * @Data
 * @Entity
 * @Table(name = "client_careers")
 * // 這個註解將繼承來的 'id' 欄位，在資料庫表格中重新命名為 'client_id'。
 * @AttributeOverride(name = "id", column = @Column(name = "client_id"))
 * public class ClientCareerEntity extends ClientBaseEntity {
 *     // ... 其他欄位
 * }
 * }</pre>
 *
 * @see rocks.ifa.spring.domain.clientProfile.ClientProfileEntity
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class ClientBaseEntity {

    /**
     * 主鍵。對於子類別而言，此 ID 同時也作為指向 {@code client_profiles.id} 的外鍵。
     * 在 Java 程式碼中，此欄位統一命名為 'id'。
     * 在子類別中，可以使用 {@code @AttributeOverride} 來覆寫其在資料庫中的欄位名。
     */
    @Id
    private UUID id;

    /**
     * 建立或最後修改此紀錄的顧問的 Firebase UID。
     */
    @JsonIgnore
    @Column(name = "agent_firebase_uid", nullable = false)
    private String agentFirebaseUid;

    /**
     * 此實體被建立時的時間戳。由 Hibernate 自動管理。
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime createdAt;

    /**
     * 此實體被最後更新時的時間戳。由 Hibernate 自動管理。
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime updatedAt;
}
