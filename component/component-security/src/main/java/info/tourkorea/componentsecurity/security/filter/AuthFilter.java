package info.tourkorea.componentsecurity.security.filter;

import info.tourkorea.componentsecurity.security.AuthService;

import info.tourkorea.componentsecurity.security.util.JwtTokenUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import response.ErrorCode;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("================================================== AUTH FILTER ===============================================");
        if (request.getMethod().equals("OPTIONS")) return ;

        try {
            String token = resolveToken(request);
            if (!StringUtils.hasText(token)) throw new JwtException(ErrorCode.JWT_NOTFOUND.getErrorMsg());

            jwtTokenUtil.validateToken(token);
            Authentication authentication = authService.generateAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 '{}' 정보 저장", authentication.getName());
        } catch (JwtException e) {
            log.info(e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

}
