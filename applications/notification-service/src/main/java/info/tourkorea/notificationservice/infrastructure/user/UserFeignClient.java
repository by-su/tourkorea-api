package info.tourkorea.notificationservice.infrastructure.user;

import info.tourkorea.notificationservice.dto.notification.NotificationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import response.ApiResponse;

import java.util.List;

@Component
@FeignClient(name = "user-service")
public interface UserFeignClient {

    @GetMapping(value = "/users/{userId}")
    ApiResponse<NotificationResponse.UserDetail> getUserInfo(@RequestParam("userId") Long userId);

    @GetMapping(value = "/users")
    List<NotificationResponse.UserDetail> getUsers(@RequestParam("ids") List<Long> userIds);


}
