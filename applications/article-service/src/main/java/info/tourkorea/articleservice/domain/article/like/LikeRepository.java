package info.tourkorea.articleservice.domain.article.like;

import info.tourkorea.articleservice.domain.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Liked, Long> {

    Optional<Liked> findByUserIdAndArticleAndDeletedFalse(Long userId, Article article);

    Optional<Liked> findByUserIdAndArticle(Long userId, Article article);
}
