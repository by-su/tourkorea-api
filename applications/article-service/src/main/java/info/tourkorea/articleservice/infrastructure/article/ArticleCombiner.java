package info.tourkorea.articleservice.infrastructure.article;

import info.tourkorea.articleservice.dto.article.ArticleDTO;
import info.tourkorea.articleservice.dto.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArticleCombiner {
    public void combineArticleDTOandUser(Page<ArticleDTO.ArticleQueryDTO> articles, List<UserResponse.UserDetail> users) {
        final List<ArticleDTO.ArticleQueryDTO> contents = articles.getContent();

    }

}
