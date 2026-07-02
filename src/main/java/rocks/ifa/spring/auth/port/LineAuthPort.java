package rocks.ifa.spring.auth.port;

import rocks.ifa.spring.auth.dtos.LineTokenPayload;

import java.util.Optional;

public interface LineAuthPort {
    Optional<LineTokenPayload> verifyIdToken(String idToken, String clientId);
}
