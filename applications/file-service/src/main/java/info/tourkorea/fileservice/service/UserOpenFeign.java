package info.tourkorea.fileservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "user-service", url = "${feign-url.user-service}")
public interface UserOpenFeign {

    @PatchMapping(value =  "/users/settings/profile-image", consumes = "application/json")
    void update(@RequestParam String imgPath);
}
