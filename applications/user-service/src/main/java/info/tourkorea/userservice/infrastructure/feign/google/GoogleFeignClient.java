package info.tourkorea.userservice.infrastructure.feign.google;

import feign.Param;
import info.tourkorea.userservice.dto.user.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "google", url = "https://oauth2.googleapis.com", configuration = GoogleConfig.class)
public interface GoogleFeignClient {

    @GetMapping("/tokeninfo")
    UserResponse.GoogleUser googleLogin(@RequestParam("id_token") String idToken);
}
