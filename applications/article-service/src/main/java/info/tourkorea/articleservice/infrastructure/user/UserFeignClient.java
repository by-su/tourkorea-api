package info.tourkorea.articleservice.infrastructure.user;

import info.tourkorea.articleservice.dto.user.UserResponse;
import lombok.Getter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import response.ApiResponse;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static info.tourkorea.articleservice.dto.user.UserResponse.*;

@Component
@FeignClient(name = "userinfo", url = "${feign-url.user-service}")
public interface UserFeignClient {

    @GetMapping("/users")
    Set<UserDetail> getUsers(@RequestParam("ids") Collection<Long> ids);

    @GetMapping("/users/{id}")
    ApiResponse<UserDetail> getUser(@PathVariable("id") Long id);

}
