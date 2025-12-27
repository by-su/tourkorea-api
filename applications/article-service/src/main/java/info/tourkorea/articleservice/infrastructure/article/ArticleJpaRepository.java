package info.tourkorea.articleservice.infrastructure.article;

import info.tourkorea.articleservice.domain.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleJpaRepository extends JpaRepository<Article, Long> {

    Optional<Article> findByIdAndDeletedFalse(Long id);

}
