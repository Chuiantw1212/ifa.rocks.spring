package rocks.ifa.spring.infrastructure.config;

import com.google.firebase.auth.UserRecord;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public class SecurityUtils {

    /**
     * Gets the Firebase UID of the currently authenticated user from the SecurityContext.
     * This is the single source of truth for the current user's UID.
     * It relies on FirebaseTokenFilter correctly setting the UserRecord as the principal.
     *
     * @return The Firebase UID of the current user.
     * @throws ResponseStatusException if the user is not authenticated or the principal is not a UserRecord.
     */
    public static String getCurrentUserUid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserRecord) {
            return ((UserRecord) principal).getUid();
        }

        // If the principal is not a UserRecord, something is wrong in the filter chain.
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid authentication principal type. Expected UserRecord, but got " + principal.getClass().getName());
    }
}
