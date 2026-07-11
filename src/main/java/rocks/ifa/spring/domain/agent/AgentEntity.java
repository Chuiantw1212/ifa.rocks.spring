package rocks.ifa.spring.domain.agent;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Agent (顧問) 聚合根 (Aggregate Root).
 * 負責維護 Agent 的核心業務規則與狀態一致性。
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA needs a no-arg constructor
@Entity
@Table(name = "agents", indexes = {
    @Index(name = "idx_agent_firebase_uid", columnList = "firebaseUid", unique = true),
    @Index(name = "idx_agent_line_user_id", columnList = "lineUserId", unique = true),
    @Index(name = "idx_agent_email", columnList = "email", unique = true)
})
public class AgentEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String firebaseUid;

    private String lineUserId;

    private String email;

    private String name;

    private String pictureUrl;

    // --- Constructors ---

    /**
     * Factory method for creating a new Agent from a Firebase login.
     */
    public static AgentEntity createWithFirebase(String firebaseUid, String email, String name, String pictureUrl) {
        Assert.hasText(firebaseUid, "Firebase UID cannot be blank.");
        AgentEntity agent = new AgentEntity();
        agent.firebaseUid = firebaseUid;
        agent.email = email; // Email can be null from Firebase
        agent.name = name;
        agent.pictureUrl = pictureUrl;
        return agent;
    }

    // --- Business Methods (Mutators) ---

    /**
     * Links a LINE account to this agent.
     * If the agent already has a LINE account, it must match the provided one.
     * It also updates the profile information from the LINE data.
     */
    public void linkLineAccount(String lineUserId, String email, String name, String pictureUrl) {
        Assert.hasText(lineUserId, "LINE User ID cannot be blank.");

        if (StringUtils.hasText(this.lineUserId) && !this.lineUserId.equals(lineUserId)) {
            throw new IllegalStateException("Agent is already bound to a different LINE user.");
        }
        
        this.lineUserId = lineUserId;
        updateProfile(name, pictureUrl);

        if (!StringUtils.hasText(this.email) && StringUtils.hasText(email)) {
            this.email = email;
        }
    }

    /**
     * Links a Firebase account to this agent.
     * If the agent already has a Firebase account, it must match the provided one.
     */
    public void linkFirebaseAccount(String firebaseUid) {
        Assert.hasText(firebaseUid, "Firebase UID cannot be blank.");

        if (StringUtils.hasText(this.firebaseUid) && !this.firebaseUid.equals(firebaseUid)) {
            throw new IllegalStateException("Agent is already bound to a different Firebase user.");
        }
        this.firebaseUid = firebaseUid;
    }

    /**
     * Updates the agent's profile information.
     * Ensures that blank values do not overwrite existing data.
     */
    public void updateProfile(String name, String pictureUrl) {
        if (StringUtils.hasText(name)) {
            this.name = name;
        }
        if (StringUtils.hasText(pictureUrl)) {
            this.pictureUrl = pictureUrl;
        }
    }
}
