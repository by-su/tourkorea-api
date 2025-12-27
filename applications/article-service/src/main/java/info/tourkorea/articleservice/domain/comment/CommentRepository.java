package info.tourkorea.articleservice.domain.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleIdAndDeletedFalse(Long articleId);

    Page<Comment> findByCreatedByAndDeletedFalse(Long userId, Pageable pageable);

    Optional<Comment> findByIdAndDeletedFalse(Long commentId);
}
