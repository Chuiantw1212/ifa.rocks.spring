package rocks.ifa.spring.infrastructure.persistence.po;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter // POs can have setters for mapping convenience
@Entity
@Table(name = "agents", indexes = {
    @Index(name = "idx_agent_firebase_uid", columnList = "firebaseUid", unique = true),
    @Index(name = "idx_agent_line_user_id", columnList = "lineUserId", unique = true),
    @Index(name = "idx_agent_email", columnList = "email", unique = true)
})
public class AgentPO {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String firebaseUid;

    private String lineUserId;

    private String email;

    private String name;

    private String pictureUrl;
}
