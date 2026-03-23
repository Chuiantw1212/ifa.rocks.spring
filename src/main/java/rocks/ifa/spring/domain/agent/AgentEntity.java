package rocks.ifa.spring.domain.agent;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * Represents an Agent (IFA Advisor) in the local database.
 * This entity is typically synced with Firebase Auth user records.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "agents")
public class AgentEntity {

    /**
     * The primary key, which is the Firebase UID.
     */
    @Id
    @Column(nullable = false, updatable = false)
    private String uid;

    /**
     * The agent's email address.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * The agent's display name.
     */
    private String displayName;

    /**
     * Whether the agent's account is disabled.
     */
    private boolean disabled;

    /**
     * The timestamp when the agent was created in Firebase.
     */
    private OffsetDateTime creationTimestamp;

    /**
     * The timestamp of the agent's last sign-in.
     */
    private OffsetDateTime lastSignInTimestamp;
}
