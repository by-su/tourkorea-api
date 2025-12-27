package info.tourkorea.articleservice.domain.article.like;

import info.tourkorea.articleservice.domain.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DislikeRepository extends JpaRepository<Dislike, Long> {
    Optional<Dislike> findByUserIdAndArticleAndDeletedFalse(Long userId, Article article);

    Optional<Dislike> findByUserIdAndArticle(Long id, Article article);
}
