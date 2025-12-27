package info.tourkorea.userservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import static info.tourkorea.userservice.domain.user.UserDTO.*;

public class UserRequest {

    public record RegisterRequest(
            @Schema(description = "이메일 주소", defaultValue = "bearsu98@gmail.com")
            @NotBlank(message = "Please provide a valid email address")
            @Email
            String email,
            @Schema(description = "비밀번호", defaultValue = "password123")
            @NotBlank(message = "Please provide a valid password")
            String password
    ) {

        @Builder
        public RegisterRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public RegisterDTO toDTO(String encodedPassword, String username) {
            return RegisterDTO.builder()
                    .email(email)
                    .username(username)
                    .encodedPassword(encodedPassword)
                    .build();
        }
    }


    public record LoginRequest(
            @Schema(description = "이메일", defaultValue = "bearsu98@gmail.com") @NotBlank @Email String email,
            @Schema(description = "최소 8자 이상 비밀번호", defaultValue = "password123") @Min(8) @Max(64) @NotBlank String password) {
        @Builder
        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public LoginDTO toDTO() {
            return LoginDTO.builder()
                    .email(this.email)
                    .rawPassword(this.password)
                    .build();
        }
    }

    public record PasswordResetRequestForm(
            @Schema(description = "이메일", defaultValue = "bearsu98@gmail.com")
            @NotBlank
            @Email String email
    ) {
    }

    public record PasswordChangeRequestForm(
            @Schema(description = "이메일 쿼리 파라미터로 전송된 UUID 링크", example = "123e4567-e89b-12d3-a456-426614174000")
            String uuid,
            @Schema(description = "새로운 비밀번호", defaultValue = "newPassword123")
            String password
    ) {

    }

    public record SettingsRequest(
            String username,
            String countryCode,
            String birth,
            String description
    ) {
    }

    @Getter
    public static class MyPageRequest {
        private String type = "article";
        private Integer page = 0;
        private Integer size = 10;

        public MyPageRequest() {}

        public MyPageRequest(String type, Integer page, Integer size) {
            this.type = type;
            this.page = page;
            this.size = size;
        }
    }
}