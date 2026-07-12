package rocks.ifa.spring.domain.agent;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "agents")
public class Agent {

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

    // Factory Method
    public static Agent createWithFirebase(String firebaseUid, String email, String name, String pictureUrl) {
        Assert.hasText(firebaseUid, "Firebase UID cannot be blank.");
        Agent agent = new Agent();
        agent.setFirebaseUid(firebaseUid);
        agent.setEmail(email);
        agent.setName(name);
        agent.setPictureUrl(pictureUrl);
        return agent;
    }

    // Business Methods
    public void linkFirebaseAccount(String firebaseUid) {
        if (StringUtils.hasText(firebaseUid)) {
            this.firebaseUid = firebaseUid;
        }
    }

    public void updateProfile(String name, String pictureUrl) {
        if (StringUtils.hasText(name)) {
            this.name = name;
        }
        if (StringUtils.hasText(pictureUrl)) {
            this.pictureUrl = pictureUrl;
        }
    }

    public void linkLineAccount(String lineUserId, String email, String name, String pictureUrl) {
        if (StringUtils.hasText(lineUserId)) {
            this.lineUserId = lineUserId;
        }
        if (!StringUtils.hasText(this.email) && StringUtils.hasText(email)) {
            this.email = email;
        }
        updateProfile(name, pictureUrl);
    }
}
