package rocks.ifa.spring.common.config;

import com.google.firebase.auth.UserRecord;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public class SecurityUtils {

    public static String getCurrentUserUid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserRecord) {
            return ((UserRecord) principal).getUid();
        }

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid authentication principal type");
    }
}
