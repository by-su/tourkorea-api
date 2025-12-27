package info.tourkorea.articleservice.infrastructure.article;

import org.springframework.stereotype.Component;

import java.util.List;

import static info.tourkorea.articleservice.dto.article.ArticleDTO.*;

@Component
public class ArticleAuthorExtractor {

    public List<Long> extractUserIds(List<ArticleQueryDTO> articles) {
        return articles
                .stream()
                .map(ArticleQueryDTO::getCreatedBy)
                .toList();
    }
}
