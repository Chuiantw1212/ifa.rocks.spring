package rocks.ifa.spring.auth.port;

// Corrected import
import rocks.ifa.spring.domain.line.LineTokenPayload;

import java.util.Optional;

public interface LineAuthPort {
    /**
     * Verifies a LINE ID Token and returns its payload if valid.
     *
     * @param idToken The raw ID Token string from the client.
     * @return An Optional containing the verified payload, or empty if verification fails.
     */
    Optional<LineTokenPayload> verifyIdToken(String idToken);
}
