package rocks.ifa.spring.auth.port;

import rocks.ifa.spring.auth.dtos.LineTokenPayload;

import java.util.Optional;

public interface LineAuthPort {
    // clientId is now handled by the adapter implementation
    Optional<LineTokenPayload> verifyIdToken(String idToken);
}
