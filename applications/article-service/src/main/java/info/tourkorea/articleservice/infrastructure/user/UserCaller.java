package info.tourkorea.articleservice.infrastructure.user;

import info.tourkorea.articleservice.dto.article.ArticleDTO;
import info.tourkorea.articleservice.infrastructure.article.ArticleAuthorExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static info.tourkorea.articleservice.dto.article.ArticleDTO.*;
import static info.tourkorea.articleservice.dto.user.UserResponse.UserDetail;

@Component
@RequiredArgsConstructor
public class UserCaller {

    private final UserFeignClient userFeignClient;
    private final ArticleAuthorExtractor articleAuthorExtractor;

    public final Set<UserDetail> getUserSetFromArticleQuery(List<ArticleQueryDTO> contents) {
        final var userIds = contents.stream()
                .map(ArticleQueryDTO::getCreatedBy)
                .toList();
        return userFeignClient.getUsers(userIds);
    }

    public final Set<UserDetail> getUserSet(List<MyPageArticleQueryDTO> articles) {
        Set<Long> userIds = articles.stream()
                .map(MyPageArticleQueryDTO::getCreatedBy)
                .collect(Collectors.toSet());

        return userFeignClient.getUsers(userIds);
    }
}
