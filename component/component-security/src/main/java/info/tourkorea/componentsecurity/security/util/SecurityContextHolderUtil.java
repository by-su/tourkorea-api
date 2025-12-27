package info.tourkorea.componentsecurity.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import request.UserContext;

import java.util.Optional;

public class SecurityContextHolderUtil {

    public static Optional<UserContext> getUserContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == "anonymousUser") {
            return Optional.empty();
        }

        UserContext userContext = (UserContext) authentication.getPrincipal();
        return Optional.of(userContext);
    }
}
