package info.tourkorea.userservice.application;

import info.tourkorea.componentemail.MailSenderService;
import info.tourkorea.exception.AuthorizationFailException;
import info.tourkorea.componentsecurity.security.util.JwtTokenUtil;
import info.tourkorea.userservice.domain.email.EmailToken;
import info.tourkorea.userservice.domain.email.EmailTokenService;
import info.tourkorea.userservice.domain.user.User;
import info.tourkorea.userservice.domain.user.UserDTO;
import info.tourkorea.userservice.domain.user.UserService;
import info.tourkorea.userservice.dto.UserResponse;
import info.tourkorea.userservice.dto.request.PasswordUpdateRequest;
import info.tourkorea.userservice.infrastructure.feign.facebook.FacebookClient;
import info.tourkorea.userservice.infrastructure.feign.google.GoogleFeignClient;
import info.tourkorea.userservice.infrastructure.feign.article.ArticleFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import request.UserContext;
import response.ErrorCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static info.tourkorea.userservice.dto.request.UserRequest.*;
import static info.tourkorea.userservice.dto.user.UserResponse.*;
import static utils.RandomStringGenerator.generateRandomString;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailTokenService emailTokenService;
    private final GoogleFeignClient googleFeignClient;
    private final FacebookClient facebookClient;
    private final MailSenderService mailSenderService;
    private final ArticleFeign articleFeign;
    private final JwtTokenUtil jwtTokenUtil;
    private static final String FACEBOOK_FIELDS_SCOPE = "id,name,email";

    public List<UserDetail> getUsers(List<Long> ids) {
        List<User> users = userService.getUsers(ids);
        return users.stream()
                .map(UserDetail::toResponse)
                .toList();
    }


    // 등록하기 전에 Email에 대한 유효성 체크를 하기 때문에 해당 메서드에서는 유효성 검사를 진행하지 않아도 된다.
    @Transactional
    public UserDetail registerUser(RegisterRequest form) {

        UserDTO.RegisterDTO registerDTO = form.toDTO(
                encodePassword(form.password()),
                generateRandomString()
        );

        User user = userService.registerUser(registerDTO);

        return UserDetail.toResponse(user);
    }

    public void checkEmail(String email) {
        userService.checkEmail(email);
    }

    public UserLoginResponse login(LoginRequest request) {

        UserDTO.LoginDTO loginDTO = request.toDTO();

        User user = userService.login(loginDTO);

        var claims = generateClaims(user);
        String token = jwtTokenUtil.generateAccessToken(claims);
        return UserLoginResponse.from(user, token);
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Transactional
    public void leave(String email) {
        userService.leave(email);
    }

    @Transactional
    public void sendPasswordResetEmail(PasswordResetRequestForm form) {

        userService.isExist(form.email());

        var link = UUID.randomUUID().toString();
        EmailToken emailToken = emailTokenService.save(
                EmailToken.builder()
                    .email(form.email())
                    .link(link)
                    .build()
        );

        mailSenderService.sendPasswordResetEmail(emailToken.getEmail(), emailToken.getLink());
    }

    @Transactional
    public void changePassword(PasswordChangeRequestForm passwordChangeRequestForm) {
        String link = passwordChangeRequestForm.uuid();
        EmailToken emailToken = emailTokenService.findByLink(link);

        User user = userService.findByEmail(emailToken.getEmail());
        user.changePassword(passwordEncoder.encode(passwordChangeRequestForm.password()));
    }

    @Transactional
    public UserLoginResponse googleLogin(String accessToken) {
        GoogleUser googleUser = googleFeignClient.googleLogin(accessToken);
        User user = userService.googleLogin(googleUser.toDTO(generateRandomString()));
        var claims = generateClaims(user);
        String token = jwtTokenUtil.generateAccessToken(claims);
        return UserLoginResponse.from(user, token);
    }

    @Transactional
    public UserLoginResponse facebookLogin(String accessToken) {
        UserResponse.FacebookUser facebookUser = facebookClient.facebookLogin(accessToken, FACEBOOK_FIELDS_SCOPE);

        User user = userService.facebookLogin(facebookUser.toDTO(generateRandomString()));
        var claims = generateClaims(user);
        String token = jwtTokenUtil.generateAccessToken(claims);
        return UserLoginResponse.from(user, token);
    }

    private Map<String, String> generateClaims(User user) {
        var map = new HashMap<String, String>();
        map.put("user_id", user.getId().toString());
        map.put("username", user.getUsername());
        map.put("profile_image", user.getProfileImageUrl());
        map.put("countryCode", user.getCountryCode());
        map.put("description", user.getDescription());
        return map;
    }

    public MyPageArticleResponse getUserDetail(UserContext userContext, String type, int page, int size) {
        return articleFeign.getProfileContents(type, page, size);
    }

    public void isAvailable(String newUsername) {
        userService.checkIfDuplicatedUsername(newUsername);
    }

    @Transactional
    public UserDetail updateSettings(UserContext userContext, SettingsRequest request) {
        User user = userService.updateSettings(userContext.getId(), request);
        return UserDetail.toResponse(user);
    }

    public UserDetail getUserInfo(Long userId) {
        User user = userService.findById(userId);
        return UserDetail.toResponse(user);
    }

    public UserDetail getMyInfo(UserContext userContext) {
        if (userContext == null || userContext.getToken() == null) {
            throw new AuthorizationFailException(ErrorCode.AUTHORIZATION_FAIL);
        }
        User user = userService.findById(userContext.getId());
        return UserDetail.toResponse(user);
    }

    @Transactional
    public void updatePassword(PasswordUpdateRequest request) {
        userService.updatePassword(request);
    }

    @Transactional
    public void updateProfileImage(String imgPath) {
        userService.updateProfileImage(imgPath);
    }
}
