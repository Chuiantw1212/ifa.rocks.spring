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
     * @return The Firebase UID.
     */
    public static String getCurrentUserUid() {
        return getCurrentUserRecord().getUid();
    }

    /**
     * Gets the full UserRecord of the currently authenticated user.
     * @return The Firebase UserRecord.
     */
    public static UserRecord getCurrentUserRecord() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserRecord) {
            return (UserRecord) principal;
        } else if (principal instanceof AuthRes) {
            // This case handles the initial login where the filter might store AuthRes
            return ((AuthRes) principal).agent() != null 
                ? new UserRecord.UpdateRequest(((AuthRes) principal).agent().uid()).getPhotoUrl() // This is a workaround to get a UserRecord-like object
                : null; // Or handle client case appropriately
        }
        
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid authentication principal type");
    }

    private static Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }
        return authentication;
    }
}
