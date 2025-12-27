package info.tourkorea.componentjpa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import request.UserContext;

import java.util.Optional;

@Configuration
public class AuditorConfig implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserContext) {
                UserContext userContext = (UserContext) principal;
                return Optional.of(userContext.getId());
            } else {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }


}