package rocks.ifa.spring.domain.agent;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

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
}
