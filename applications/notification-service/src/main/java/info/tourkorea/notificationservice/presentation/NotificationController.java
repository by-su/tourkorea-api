package info.tourkorea.notificationservice.presentation;

import info.tourkorea.notificationservice.application.NotificationFacade;
import info.tourkorea.notificationservice.dto.notification.NotificationRequest;
import info.tourkorea.notificationservice.dto.notification.NotificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import response.ApiResponse;

@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationFacade notificationFacade;

    // config 파일을 읽어오기 위한 환경 변수
    private final Environment env;

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in User Service"
                + ", port(server.port)=" + env.getProperty("server.port")
                + ", token secret=" + env.getProperty("token.secret")
                + ", token expiration time=" + env.getProperty("token.expiration_time"));
    }
    // 알림 생성 api
    @PostMapping
    public void createNotification(@RequestBody NotificationRequest.RegisterRequest form) {
        notificationFacade.createNotification(form);
    }

    // 알림 조회 api
    @GetMapping
    public ApiResponse<NotificationResponse.MyNotifications> getNotification(Pageable pageable) {
        NotificationResponse.MyNotifications notifications = notificationFacade.getNotification(pageable);
        return ApiResponse.success(HttpStatus.OK.name(), notifications);
    }

    // 알림 삭제 api
    @DeleteMapping("/{notificationId}")
    public ApiResponse<String> deleteNotification(@Parameter(description = "알림 삭제할 ID") @PathVariable Long notificationId) {
        notificationFacade.deleteNotification(notificationId);
        return ApiResponse.success(HttpStatus.OK.name(), null, "The notification has been deleted");
    }

    // 알림 읽기 api
    @PatchMapping("/{notificationId}")
    public ApiResponse<String> readNotification(@Parameter(description = "알림 읽기할 ID") @PathVariable Long notificationId) {
        notificationFacade.readNotification(notificationId);
        return ApiResponse.success(HttpStatus.OK.name(), null, "The notification has been read");
    }

    // 모든 알림 삭제
    @DeleteMapping
    public ApiResponse<String> deleteNotifications() {
        notificationFacade.deleteNotifications();
        return ApiResponse.success(HttpStatus.OK.name(), null, "The notifications have been deleted");
    }

    // 읽을 알림이 있는지 확인하는 api (알림 아이콘에 빨간색 표시)
    @GetMapping("/check")
    public ApiResponse<Boolean> checkUnreadNotification() {
        Boolean result = notificationFacade.checkUnreadNotification();
        return ApiResponse.success(HttpStatus.OK.name(), result);
    }
}
