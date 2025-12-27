package info.tourkorea.userservice.presentation;

import info.tourkorea.userservice.application.UserFacade;
import info.tourkorea.userservice.dto.request.PasswordUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import request.UserContext;
import response.ApiResponse;

import java.util.List;

import static info.tourkorea.userservice.dto.request.UserRequest.*;
import static info.tourkorea.userservice.dto.user.UserResponse.UserDetail;
import static info.tourkorea.userservice.dto.user.UserResponse.UserLoginResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserFacade userFacade;
    private static final String EMAIL_SENT_MSG = "The email has been sent";
    private static final String PASSWORD_CHANGED_MSG = "The password has been changed";

    // config 파일을 읽어오기 위한 환경 변수
    private final Environment env;

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in User Service"
                + ", port(server.port)=" + env.getProperty("server.port")
                + ", token secret=" + env.getProperty("token.secret")
                + ", token expiration time=" + env.getProperty("token.expiration_time"));
    }

    @GetMapping("/me")
    public ApiResponse<UserDetail> getMyInfo(@AuthenticationPrincipal UserContext userContext) {
        UserDetail userDetail = userFacade.getMyInfo(userContext);
        return ApiResponse.success(HttpStatus.OK.name(), userDetail);
    }

    @GetMapping("/email")
    @Operation(summary = "이메일 중복 확인")
    public ApiResponse<?> checkEmail(@Parameter(description = "사용자가 입력한 이메일") String email) {
        userFacade.checkEmail(email);
        return ApiResponse.success(HttpStatus.OK.name(), null, "The email is available");
    }

    @GetMapping("/username")
    public ApiResponse<String> checkUsernameIsAvailable(
            String newUsername
    ) {
        userFacade.isAvailable(newUsername);
        return ApiResponse.success(HttpStatus.OK.name(), null, "The username is available");
    }


    @PostMapping
    @Operation(summary = "회원가입")
    public ApiResponse<UserDetail> register(@RequestBody @Valid RegisterRequest request) {
        UserDetail userDetail = userFacade.registerUser(request);
        return ApiResponse.success(HttpStatus.CREATED.name(), userDetail);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ApiResponse<UserLoginResponse> login(@RequestBody LoginRequest request) {
        UserLoginResponse response = userFacade.login(request);
        return ApiResponse.success(HttpStatus.OK.name(), response);
    }

    @GetMapping("/login/google")
    @Operation(summary = "구글 로그인")
    public ApiResponse<UserLoginResponse> googleLogin(@RequestParam("credential") String accessToken) {
        UserLoginResponse userLoginResponse = userFacade.googleLogin(accessToken);
        return ApiResponse.success(HttpStatus.OK.name(), userLoginResponse);
    }

    @GetMapping("/login/facebook")
    @Operation(summary = "페이스북 로그인")
    public ApiResponse<UserLoginResponse> facebookLogin(String access_token) {
        UserLoginResponse userLoginResponse = userFacade.facebookLogin(access_token);
        return ApiResponse.success(HttpStatus.OK.name(), userLoginResponse);
    }

    @PostMapping("/email")
    @Operation(summary = "비밀번호 재설정 이메일 전송")
    public ApiResponse<String> sendResetEmail(@RequestBody PasswordResetRequestForm form) {
        userFacade.sendPasswordResetEmail(form);
        return ApiResponse.success(HttpStatus.OK.name(), null, EMAIL_SENT_MSG);
    }

    @PatchMapping("/password")
    @Operation(summary = "비밀번호 변경")
    public ApiResponse<String> resetPassword(@RequestBody PasswordChangeRequestForm form) {
        userFacade.changePassword(form);
        return ApiResponse.success(HttpStatus.OK.name(), null, PASSWORD_CHANGED_MSG);
    }

    // Todo : 최적화 -> 비동기 사용, 페이징 적용
    @GetMapping("/mypage")
    @Operation(summary = "마이페이지 정보 조회", description = "토큰 필수")
    public ApiResponse<?> getMyPageInfo(
            String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserContext userContext
    ) {
        var response = userFacade.getUserDetail(userContext, type, page, size);
        return ApiResponse.success(HttpStatus.OK.name(), response);
    }

    @PatchMapping("/settings")
    public ApiResponse<UserDetail> updateSettings(
            @RequestBody @Valid SettingsRequest request,
            @AuthenticationPrincipal UserContext userContext
    ) {
        var userDetail = userFacade.updateSettings(userContext, request);
        return ApiResponse.success(HttpStatus.OK.name(), userDetail, "The settings have been updated");
    }

    @GetMapping("/{userId}")
    @Operation(summary = "사용자 정보 조회")
    public ApiResponse<UserDetail> getUserInfo(@PathVariable Long userId) {
        UserDetail userDetail = userFacade.getUserInfo(userId);
        return ApiResponse.success(HttpStatus.OK.name(), userDetail);
    }

    @GetMapping
    @Operation(summary = "사용자 리스트 정보 조회 (다른 서비스로부터)")
    public List<UserDetail> getUsers(@RequestParam List<Long> ids) {
        return userFacade.getUsers(ids);
    }

    @PatchMapping("/settings/password")
    public ApiResponse<?> updatePassword(@RequestBody PasswordUpdateRequest request) {
        userFacade.updatePassword(request);
        return ApiResponse.success(HttpStatus.OK.name(), null, "The password has been updated");
    }

    @PatchMapping("/settings/profile-image")
    public ApiResponse<?> updateProfileImage(@RequestParam String imgPath) {
        userFacade.updateProfileImage(imgPath);
        return ApiResponse.success(HttpStatus.OK.name(), null, "The profile image has been updated");
    }

}
