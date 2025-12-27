package info.tourkorea.userservice.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class UserDTO {

    @Builder
    public record RegisterDTO(String email, String username, String encodedPassword) {
        public User toEntity() {
            return User.builder()
                    .email(this.email())
                    .username(this.username())
                    .password(this.encodedPassword())
                    .build();
        }
    }

    @Getter
    public static class LoginDTO {
        private final String email;
        private final String rawPassword;

        @Builder
        public LoginDTO(String email, String rawPassword) {
            this.email = email;
            this.rawPassword = rawPassword;
        }
    }

    @Getter
    public static class GoogleLoginDTO {
        private final String email;
        private final String name;
        private final String profileImageUrl;

        @Builder
        public GoogleLoginDTO(String email, String name, String profileImageUrl) {
            this.email = email;
            this.name = name;
            this.profileImageUrl = profileImageUrl;
        }

        public User toEntity() {
            return User.builder()
                    .email(this.getEmail())
                    .username(this.getName())
                    .google(true)
                    .profileImageUrl("https://tour-korea.s3.ap-northeast-2.amazonaws.com/profile/default.png")
                    .build();
        }
    }

    @Getter
    @ToString
    public static class FacebookLoginDTO {
        private final String email;
        private final String name;
        private final String profileImageUrl;

        @Builder
        public FacebookLoginDTO(String email, String name, String profileImageUrl) {
            this.email = email;
            this.name = name;
            this.profileImageUrl = profileImageUrl;
        }



        public User toEntity() {
            return User.builder()
                    .email(this.getEmail())
                    .username(this.getName())
                    .profileImageUrl("https://tour-korea.s3.ap-northeast-2.amazonaws.com/profile/default.png")
                    .build();
        }
    }
}
