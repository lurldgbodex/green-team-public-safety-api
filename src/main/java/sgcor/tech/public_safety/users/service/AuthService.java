package sgcor.tech.public_safety.users.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sgcor.tech.public_safety.exception.UnauthorizedException;
import sgcor.tech.public_safety.users.entity.User;

@Slf4j
@Service
public class AuthService {
    public static User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("ContextHolder: " + authentication);

        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        } throw new UnauthorizedException("No authenticated user found");
    }
}
