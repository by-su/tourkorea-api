package info.tourkorea.notificationservice.dto.notification;

import info.tourkorea.notificationservice.domain.notification.Notification;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationResponse {

    @Getter
    @ToString
    @Builder
    public static class MyNotifications {
        List<NotificationInfo> notifications;

        public static MyNotifications toResponse(List<Notification> notifications) {
            List<NotificationInfo> collect = notifications.stream()
                    .map(NotificationInfo::from)
                    .collect(Collectors.toList());

            return MyNotifications.builder()
                    .notifications(collect)
                    .build();
        }
    }

    @Getter
    @ToString
    @Builder
    public static class NotificationInfo {
        private final Long id;
        private final Long userId;
        private final Long senderId;
        private final String username;
        private final String sendername;
        private final Long contentId;
        private final String comment;
        private final String type;
        private final boolean readYn;

        public static NotificationInfo from(Notification notification) {
            return NotificationInfo.builder()
                    .id(notification.getId())
                    .userId(notification.getUserId())
                    .senderId(notification.getSenderId())
                    .username(notification.getUsername())
                    .sendername(notification.getSendername())
                    .comment(notification.getComment())
                    .contentId(notification.getContentId())
                    .type(notification.getType().name())
                    .readYn(notification.isReadYn())
                    .build();
        }
    }
    @Getter
    @ToString
    @Builder
    public static class UserDetail {
        private final Long id;
        private final String email;
        private final String username;
        private final LocalDate birth;
        private final String countryCode;

        public UserDetail(Long id, String email, String username, LocalDate birth, String countryCode) {
            this.id = id;
            this.email = email;
            this.username = username;
            this.birth = birth;
            this.countryCode = countryCode;
        }

    }




}
