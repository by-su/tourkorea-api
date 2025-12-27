package info.tourkorea.userservice.infrastructure.feign.article;

import info.tourkorea.userservice.dto.user.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "article-service", url = "${feign-url.article-service}")
public interface ArticleFeign {
    @RequestMapping(value =  "/articles/mypage", method = RequestMethod.GET)
    UserResponse.MyPageArticleResponse getProfileContents(
            @RequestParam(name = "type") String type,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size
    );

}
