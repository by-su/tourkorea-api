package info.tourkorea.articleservice.infrastructure.notification;

import info.tourkorea.articleservice.dto.notification.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "notification-client", url = "${feign-url.notification-service}")
public interface NotificationFeignClient {

    @PostMapping("/notifications")
    void createNotification(@RequestBody NotificationRequest.RegisterRequest form);
}
