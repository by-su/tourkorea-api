package info.tourkorea.userservice.dto.user;

import info.tourkorea.userservice.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import static info.tourkorea.userservice.domain.user.UserDTO.*;

public class UserResponse {

    @Getter
    @ToString
    public static class UserDetail implements Serializable {
        private final String profileImageUrl;
        private final Long id;
        private final String email;
        private final String username;
        private final LocalDate birth;
        private final String countryCode;
        private final String description;

        @Builder
        public UserDetail(String profileImageUrl, Long id, String email, String username, LocalDate birth, String countryCode, String description) {
            this.profileImageUrl = profileImageUrl;
            this.id = id;
            this.email = email;
            this.username = username;
            this.birth = birth;
            this.countryCode = countryCode;
            this.description = description;
        }



        public static UserDetail toResponse(User user) {
            return UserDetail.builder()
                    .profileImageUrl(user.getProfileImageUrl())
                    .id(user.getId())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .birth(user.getBirth())
                    .countryCode(user.getCountryCode())
                    .description(user.getDescription())
                    .build();
        }
    }

    @Getter
    public static class UserLoginResponse {
        private final UserDetail userDetail;
        private final String token;

        @Builder
        public UserLoginResponse(UserDetail userDetail, String token) {
            this.userDetail = userDetail;
            this.token = token;
        }

        public static UserLoginResponse from(User user, String token) {
            UserDetail userDetail = UserDetail.toResponse(user);
            return new UserLoginResponse(userDetail, token);
        }
    }

    @Getter
    public static class GoogleUser {
        private final String email;
        private final String name;

        @Builder
        public GoogleUser(String email, String name) {
            this.email = email;
            this.name = name;
        }

        public GoogleLoginDTO toDTO(String name) {
            return GoogleLoginDTO.builder()
                    .email(this.getEmail())
                    .name(name)
                    .build();
        }
    }

    public record Mypage(
        UserResponse.Profile profile,
        List<MyPageArticleResponse> articles,
        Page page
    ) {

    }
    @Builder
    public record MyPageArticleResponse(
            List<MyPageArticleAndUser> articles,
            UserProfile userProfile,
            response.Page page
    ) {
    }

    @Builder
    private record MyPageArticleAndUser(
            UserDetail author,
            Long id,
            String title,
            String content,
            Integer likeCount,
            Integer dislikeCount,
            Integer commentCount,
            Boolean liked,
            Boolean disliked,
            Long articleCount,
            Long bookmarkCount,
            Long createdBy,
            Long updatedBy,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt
    ) {
    }

    private record UserProfile(
            UserDetail user,
            Long articleCount,
            Long bookmarkCount
    ) {

    }

    public record Page(
            Long totalCount
    ) {

    }

    private record Profile(
            String username,
            String description,
            Long myPostCnt,
            Long savedPostCnt
    ) {
    }





}
