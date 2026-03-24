package rocks.ifa.spring.infra;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public class SecurityUtils {

    /**
     * Gets the Firebase UID of the currently authenticated user from the SecurityContext.
     * This relies on the FirebaseTokenFilter successfully populating the context.
     *
     * @return The Firebase UID of the current user.
     * @throws ResponseStatusException if the user is not authenticated or the principal is invalid.
     */
    public static String getCurrentAgentUid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            return (String) principal;
        }

        // This should not happen if FirebaseTokenFilter is working correctly.
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid authentication principal type");
    }
}
