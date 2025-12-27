package info.tourkorea.articleservice.domain.bookmark;

import info.tourkorea.articleservice.domain.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByUserIdAndArticleAndDeletedFalse(Long userId, Article article);
    Optional<Bookmark> findByUserIdAndArticle(Long userId, Article article);
}
