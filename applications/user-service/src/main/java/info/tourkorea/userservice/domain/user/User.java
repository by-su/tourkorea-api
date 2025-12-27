package info.tourkorea.userservice.domain.user;

import info.tourkorea.userservice.dto.request.UserRequest;
import info.tourkorea.userservicedomain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

import static info.tourkorea.userservice.dto.request.UserRequest.*;
import static utils.LocalDateFormatter.convertStringToLocalDate;

@Getter
@ToString
@Entity
@NoArgsConstructor
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String profileImageUrl;

    @Column(nullable = false, unique = true, length = 64)
    private String username;
    @Column(nullable = false, unique = true, length = 64)
    private String email;
    @Column(length = 500)
    private String password;
    @Column(length = 1000)
    private String description;
    private LocalDate birth;
    private String countryCode;
    private boolean google;

    @Builder
    public User(String profileImageUrl, String username, String email, String password, String description, LocalDate birth, String countryCode, Boolean google) {
        this.profileImageUrl = profileImageUrl;
        this.username = username;
        this.email = email;
        this.password = password;
        this.description = description;
        this.birth = birth;
        this.countryCode = countryCode;
        this.google = google == null ? false : google;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void leave() {
        super.setDeleted();
    }

    public void updateSettings(SettingsRequest request) {
        String birth = request.birth();
        if (birth != null) this.birth = convertStringToLocalDate(birth);

        this.username = request.username() == null ? this.username : request.username();
        this.description = request.description() == null ? this.description : request.description();
        this.countryCode = request.countryCode() == null ? this.countryCode : request.countryCode();
    }

    public void updateProfileImage(String imgPath) {
        this.profileImageUrl = imgPath;
    }
}
