package info.tourkorea.notificationservice.domain.notification;

import info.tourkorea.exception.AuthenticationFailException;
import info.tourkorea.notificationservice.dto.notification.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import request.UserContext;
import response.ErrorCode;

import java.util.List;

import static info.tourkorea.componentsecurity.security.util.SecurityContextHolderUtil.getUserContext;
import static info.tourkorea.notificationservice.dto.notification.NotificationRequest.*;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void createNotification(RegisterDTO form) {
        // Check: 알림을 보냈는지 확인하는 로직 추가함.
        if (isNotificationExists(form)) return;

        notificationRepository.save(form.toEntity());
    }

    private boolean isNotificationExists(RegisterDTO form) {
        return notificationRepository.existsByUserIdAndSenderIdAndContentIdAndType(
                form.getUserId(),
                form.getSenderId(),
                form.getContentId(),
                Notification.NotificationType.valueOf(form.getType())
        );
    }

    public List<Notification> getNotification(Pageable pageable) {
        UserContext userContext = getUserContext().orElseThrow(() -> new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL));
        return notificationRepository.findByUserIdAndDeletedFalse(userContext.getId(), pageable);
    }

    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new IllegalArgumentException("해당 알림이 존재하지 않습니다."));
        notification.setDeletedTrue();
        notificationRepository.save(notification);
    }

    public void readNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new IllegalArgumentException("해당 알림이 존재하지 않습니다."));
        notification.setReadYnTrue();
        notificationRepository.save(notification);
    }

    public void deleteNotifications() {
        UserContext userContext = getUserContext().orElseThrow(() -> new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL));
        notificationRepository.findByUserIdAndDeletedFalse(userContext.getId()).forEach(Notification::setDeletedTrue);
    }

    public Boolean checkUnreadNotification() {
        UserContext userContext = getUserContext().orElseThrow(() -> new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL));
        return notificationRepository.existsByUserIdAndReadYnFalseAndDeletedFalse(userContext.getId());
    }
}
