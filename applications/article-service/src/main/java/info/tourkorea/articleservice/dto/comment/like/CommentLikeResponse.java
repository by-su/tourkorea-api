package info.tourkorea.articleservice.dto.comment.like;

import info.tourkorea.articleservice.domain.comment.CommentLike;
import info.tourkorea.articleservice.dto.comment.CommentResponse;

public class CommentLikeResponse {

    public record CommentLikeDTO(
            Long id,
            Long userId,
            CommentResponse.CommentGeneral comment
    ) {
        public static CommentLikeDTO of(CommentLike commentLike) {
            return new CommentLikeDTO(
                    commentLike.getId(),
                    commentLike.getUserId(),
                    CommentResponse.CommentGeneral.of(commentLike.getComment())
            );
        }
    }

    public record CommentLikeNewYnDTO(
            CommentLikeDTO commentLikeDTO,
            boolean likedYn,
            boolean newYn
    ) {
        public static CommentLikeNewYnDTO of(CommentLike commentLike, boolean likedYn,boolean newYn) {
            return new CommentLikeNewYnDTO(CommentLikeDTO.of(commentLike), likedYn, newYn);
        }
    }

}
