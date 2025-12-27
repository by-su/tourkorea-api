package info.tourkorea.userservice.infrastructure.feign.facebook;

import info.tourkorea.userservice.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static info.tourkorea.userservice.dto.UserResponse.*;

@Component
@FeignClient(name = "facebook", url = "https://graph.facebook.com/v19.0", configuration = info.tourkorea.userservicebridge.feign.facebook.FacebookConfig.class)
public interface FacebookClient {

    @GetMapping("/me?fields=id,name,email")
    FacebookUser facebookLogin(
            @RequestParam("access_token") String access_token,
            @RequestParam("fields") String fields
    );

}
