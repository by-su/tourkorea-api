package info.tourkorea.articleservice.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long>{

    Optional<CommentLike> findByUserIdAndCommentAndDeletedFalse(Long userId, Comment comment);

    Optional<CommentLike> findByUserIdAndComment(Long id, Comment comment);

}
