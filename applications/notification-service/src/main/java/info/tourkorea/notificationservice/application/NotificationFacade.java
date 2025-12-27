package info.tourkorea.notificationservice.application;

import info.tourkorea.notificationservice.dto.notification.NotificationRequest;
import info.tourkorea.notificationservice.dto.notification.NotificationRequest.RegisterDTO;
import info.tourkorea.notificationservice.dto.notification.NotificationRequest.RegisterRequest;
import info.tourkorea.notificationservice.dto.notification.NotificationResponse;
import info.tourkorea.notificationservice.domain.notification.Notification;
import info.tourkorea.notificationservice.domain.notification.NotificationService;
import info.tourkorea.notificationservice.infrastructure.user.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class NotificationFacade {
    private final NotificationService notificationService;
    private final UserFeignClient userFeignClient;

    @Transactional
    public void createNotification(RegisterRequest form) {

        // 알림 받는 사람, 보내는 사람 Username 가져오기
        List<Long> userIds = List.of(form.getUserId(), form.getSenderId());
        List<NotificationResponse.UserDetail> users = userFeignClient.getUsers(userIds);
        AtomicReference<String> username = new AtomicReference<>();
        AtomicReference<String> sendername = new AtomicReference<>();

        users.forEach(user -> {
            if (user.getId().equals(form.getUserId())) {
                username.set(user.getUsername());
            } else if (user.getId().equals(form.getSenderId())) {
                sendername.set(user.getUsername());
            }
        });

        RegisterDTO registerDTO = RegisterDTO.from(form, username.get(), sendername.get());
        notificationService.createNotification(registerDTO);
    }

    public NotificationResponse.MyNotifications getNotification(Pageable pageable) {
        List<Notification> notification = notificationService.getNotification(pageable);
        return NotificationResponse.MyNotifications.toResponse(notification);
    }

    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationService.deleteNotification(notificationId);
    }

    @Transactional
    public void readNotification(Long notificationId) {
        notificationService.readNotification(notificationId);
    }

    @Transactional
    public void deleteNotifications() {
        notificationService.deleteNotifications();
    }

    public Boolean checkUnreadNotification() {
        return notificationService.checkUnreadNotification();
    }
}
