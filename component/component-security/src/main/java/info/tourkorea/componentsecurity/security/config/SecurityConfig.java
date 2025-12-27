package info.tourkorea.componentsecurity.security.config;

import info.tourkorea.componentsecurity.security.AuthService;
import info.tourkorea.componentsecurity.security.filter.AuthFilter;
import info.tourkorea.componentsecurity.security.filter.AuthFilterExceptionHandler;
import info.tourkorea.componentsecurity.security.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 스프링 시큐리티의 기본 필터를 @bean으로 등록
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthFilterExceptionHandler authFilterExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                // REST API 방식으로 CSRF 보안 토큰 생성 x
                .csrf(
                        AbstractHttpConfigurer::disable
                )
                // 세션 사용 x
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(authFilterExceptionHandler, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new AuthFilter(authService, jwtTokenUtil), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(
                        (authorize) -> authorize
                                .anyRequest().permitAll()
//                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/resources/**").permitAll()
//                                .requestMatchers(HttpMethod.OPTIONS).permitAll()
//                                .requestMatchers(HttpMethod.GET, "users/**").permitAll()
//                                .requestMatchers(HttpMethod.GET,"/articles/**").permitAll()
//                                .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                )
                .requestMatchers("/users/email")
                .requestMatchers("/users")
                .requestMatchers("/users/login/**")
                .requestMatchers("/actuator/**")
                .requestMatchers("/test");
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
