package rocks.ifa.spring.infra.security;

import com.google.firebase.auth.UserRecord;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import rocks.ifa.spring.domain.agent.contracts.AuthRes;

public class SecurityUtils {

    /**
     * Gets the Firebase UID of the currently authenticated user.
     * This is the single source of truth for the current user's UID.
     * @return The Firebase UID.
     */
    public static String getCurrentUserUid() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof AuthRes) {
            AuthRes authRes = (AuthRes) principal;
            // The token is the most reliable part of the AuthRes
            if (authRes.token() != null && authRes.agent() != null) {
                return authRes.agent().uid();
            }
            // If it's a client login, agent() will be null, but we need a UID.
            // This part might need refinement based on what UID is stored for clients.
            // For now, let's assume the token itself contains enough info,
            // but a direct UID is better.
        } else if (principal instanceof UserRecord) {
            return ((UserRecord) principal).getUid();
        }
        
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid authentication principal type: " + principal.getClass().getName());
    }

    private static Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }
        return authentication;
    }
}
