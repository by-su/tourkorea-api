package info.tourkorea.userservice.dto;

import info.tourkorea.userservice.domain.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class UserResponse {

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FacebookUser {
        private String id;
        private String name;
        private String email;

        public UserDTO.FacebookLoginDTO toDTO(String name) {
            return UserDTO.FacebookLoginDTO.builder()
                    .email(email)
                    .name(name)
                    .build();
        }
    }
}
