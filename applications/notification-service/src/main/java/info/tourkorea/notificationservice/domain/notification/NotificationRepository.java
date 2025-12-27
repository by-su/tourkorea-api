package info.tourkorea.notificationservice.domain.notification;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    List<Notification> findByUserIdAndDeletedFalse(Long userId);

    boolean existsByUserIdAndSenderIdAndContentIdAndType(Long userId, Long senderId, Long contentId, Notification.NotificationType type);

    Boolean existsByUserIdAndReadYnFalseAndDeletedFalse(Long id);
}
