package rocks.ifa.spring.domain.agent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Agent (顧問) 聚合根 (Aggregate Root).
 * 負責維護 Agent 的核心業務規則與狀態一致性。
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE) // Private constructor for internal factory use
public class Agent {

    private UUID id;
    private String firebaseUid;
    private String lineUserId;
    private String email;
    private String name;
    private String pictureUrl;

    // --- Factory Methods ---

    public static Agent createWithFirebase(String firebaseUid, String email, String name, String pictureUrl) {
        if (firebaseUid == null || firebaseUid.isBlank()) {
            throw new IllegalArgumentException("Firebase UID cannot be blank.");
        }
        Agent agent = new Agent();
        agent.firebaseUid = firebaseUid;
        agent.email = email;
        agent.name = name;
        agent.pictureUrl = pictureUrl;
        return agent;
    }

    // This method is for persistence layer to reconstruct the object
    public static Agent fromPersistence(UUID id, String firebaseUid, String lineUserId, String email, String name, String pictureUrl) {
        Agent agent = new Agent();
        agent.id = id;
        agent.firebaseUid = firebaseUid;
        agent.lineUserId = lineUserId;
        agent.email = email;
        agent.name = name;
        agent.pictureUrl = pictureUrl;
        return agent;
    }


    // --- Business Methods (Mutators) ---

    public void linkLineAccount(String lineUserId, String email, String name, String pictureUrl) {
        if (lineUserId == null || lineUserId.isBlank()) {
            throw new IllegalArgumentException("LINE User ID cannot be blank.");
        }
        if (this.lineUserId != null && !this.lineUserId.equals(lineUserId)) {
            throw new IllegalStateException("Agent is already bound to a different LINE user.");
        }
        
        this.lineUserId = lineUserId;
        updateProfile(name, pictureUrl);

        if ((this.email == null || this.email.isBlank()) && (email != null && !email.isBlank())) {
            this.email = email;
        }
    }

    public void linkFirebaseAccount(String firebaseUid) {
        if (firebaseUid == null || firebaseUid.isBlank()) {
            throw new IllegalArgumentException("Firebase UID cannot be blank.");
        }
        if (this.firebaseUid != null && !this.firebaseUid.equals(firebaseUid)) {
            throw new IllegalStateException("Agent is already bound to a different Firebase user.");
        }
        this.firebaseUid = firebaseUid;
    }

    public void updateProfile(String name, String pictureUrl) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        if (pictureUrl != null && !pictureUrl.isBlank()) {
            this.pictureUrl = pictureUrl;
        }
    }
}
