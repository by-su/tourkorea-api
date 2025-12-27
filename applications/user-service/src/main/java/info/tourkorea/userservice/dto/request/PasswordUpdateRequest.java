package info.tourkorea.userservice.dto.request;

public record PasswordUpdateRequest(
        String currentPassword,
        String newPassword
) {
}
