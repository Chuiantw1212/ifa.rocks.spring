package rocks.ifa.spring.domain.agent;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "agents", indexes = {
    @Index(name = "idx_agent_email", columnList = "email")
})
public class AgentEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(unique = true)
    private String firebaseUid;

    @Column(unique = true)
    private String lineUserId;

    @Column(unique = true)
    private String email;

    private String name;

    private String pictureUrl;
}
