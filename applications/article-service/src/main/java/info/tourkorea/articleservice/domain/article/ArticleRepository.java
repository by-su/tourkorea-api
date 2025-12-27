package info.tourkorea.articleservice.domain.article;

import info.tourkorea.articleservice.infrastructure.article.ArticleCustomRepository;
import info.tourkorea.articleservice.infrastructure.article.ArticleJpaRepository;

public interface ArticleRepository extends ArticleCustomRepository, ArticleJpaRepository {
}
