package bytech.co.tz.matundasi.common.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// Who is making the current change, for created_by / updated_by.
public final class CurrentAuditor {
    private CurrentAuditor() {
    }

    // The logged-in user's id (from the JWT), or null when there is no
    // logged-in user (a request without a valid token, or the startup seeder).
    public static String userId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return null;
    }
}
