package info.tourkorea.articleservice.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentDislikeRepository extends JpaRepository<CommentDislike, Long> {

    Optional<CommentDislike> findByUserIdAndCommentAndDeletedFalse(Long userId, Comment comment);

    Optional<CommentDislike> findByUserIdAndComment(Long id, Comment comment);
}
