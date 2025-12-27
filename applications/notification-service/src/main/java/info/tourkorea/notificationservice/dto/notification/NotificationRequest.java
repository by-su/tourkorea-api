package info.tourkorea.notificationservice.dto.notification;

import info.tourkorea.notificationservice.domain.notification.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;


public class NotificationRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequest {
        // 알림 받는 사람 ID
        @NotBlank
        @Schema(description = "알림 받는 사람 ID")
        private Long userId;

        @NotBlank
        @Schema(description = "알림 보내는 사람 ID")
        private Long senderId;

        @NotBlank
        @Schema(description = "알림 대상 글 or 댓글의 ID")
        private Long contentId;

        @Schema(description = "Type(LIKE_ON_POST, LIKE_ON_COMMENT, COMMENT_ON_POST, REPLY_ON_COMMENT) 중 하나")
        private String type;

        @Schema(description = "알림 내용 (댓글 or 답글 시에만 필요)")
        private String comment;

    }

    @Builder
    @Getter
    public static class RegisterDTO {
        private final Long userId;
        private final Long senderId;
        private final String username;
        private final String sendername;
        private final Long contentId;
        private final String comment;
        private final String type;

        public Notification toEntity() {
            return Notification.builder()
                    .userId(userId)
                    .senderId(senderId)
                    .username(username)
                    .sendername(sendername)
                    .contentId(contentId)
                    .comment(comment)
                    .type(Notification.NotificationType.valueOf(type))
                    .build();
        }

        public static RegisterDTO from(RegisterRequest dto, String username, String sendername) {
            return RegisterDTO.builder()
                    .userId(dto.getUserId())
                    .senderId(dto.getSenderId())
                    .username(username)
                    .sendername(sendername)
                    .contentId(dto.getContentId())
                    .comment(dto.getComment())
                    .type(dto.getType())
                    .build();
        }
    }

    @Getter
    public class DeleteRequest {
        @NotBlank
        @Schema(description = "알림 ID")
        private List<Long> notificationIds;
    }
}
