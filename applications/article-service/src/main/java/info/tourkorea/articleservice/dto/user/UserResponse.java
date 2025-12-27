package info.tourkorea.articleservice.dto.user;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class UserResponse {
    @Getter
    public static class UserDetail {
        private final Long id;
        private final String profileImageUrl;
        private final String email;
        private final String username;
        private final LocalDate birth;
        private final String countryCode;
        private final String description;

        @Builder
        public UserDetail(Long id, String profileImageUrl, String email, String username, LocalDate birth, String countryCode, String description) {
            this.id = id;
            this.profileImageUrl = profileImageUrl;
            this.email = email;
            this.username = username;
            this.birth = birth;
            this.countryCode = countryCode;
            this.description = description;
        }
    }
}
