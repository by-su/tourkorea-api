package info.tourkorea.componentsecurity.security;

import info.tourkorea.componentsecurity.security.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import request.UserContext;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenUtil jwtTokenUtil;

    public Authentication generateAuthentication(String token) {
        Long userId = jwtTokenUtil.getUserId(token);
        String username = jwtTokenUtil.getUsername(token);
        String profileImage = jwtTokenUtil.getProfileImage(token);

        UserContext userContext = new UserContext(userId, username, profileImage, token);

        return new UsernamePasswordAuthenticationToken(userContext, token, null);
    }

}
