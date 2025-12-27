package info.tourkorea.articleservice.dto.comment;

import info.tourkorea.articleservice.domain.comment.Comment;
import info.tourkorea.articleservice.domain.comment.CommentDislike;
import info.tourkorea.articleservice.domain.comment.CommentLike;
import info.tourkorea.articleservice.dto.user.UserResponse;
import lombok.Builder;
import request.UserContext;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommentResponse {

    public record MyPageComments(
            List<CommentGeneral> comments,
            int totalPage
    ) {
        public static MyPageComments of(List<CommentGeneral> comments, int totalPage) {
            return new MyPageComments(comments, totalPage);
        }
    }

    @Builder
    public record CommentGeneral(
            UserContext author,
            Long id,
            Long articleId,
            String content,
            int likeCount,
            int dislikeCount,
            Long createdBy,
            Long updatedBy,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt
    ) {
        public static CommentGeneral of(Comment comment, UserContext user) {
            return CommentGeneral.builder()
                    .author(user)
                    .id(comment.getId())
                    .articleId(comment.getArticle().getId())
                    .content(comment.getContent())
                    .likeCount(comment.getLikes().size())
                    .dislikeCount(comment.getDislikes().size())
                    .createdBy(comment.getCreatedBy())
                    .updatedBy(comment.getUpdatedBy())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .build();
        }

        public static CommentGeneral of(Comment comment) {
            return CommentGeneral.builder()
                    .author(null)
                    .id(comment.getId())
                    .articleId(comment.getArticle().getId())
                    .content(comment.getContent())
                    .likeCount(comment.getLikes().size())
                    .dislikeCount(comment.getDislikes().size())
                    .createdBy(comment.getCreatedBy())
                    .updatedBy(comment.getUpdatedBy())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .build();
        }

    }

    @Builder
    public record CommentOnArticle(
            UserResponse.UserDetail author,
            Long id,
            Long articleId,
            String content,
            int likeCount,
            int dislikeCount,
            Boolean liked,
            Boolean disliked,
            Long createdBy,
            Long updatedBy,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt
    ) {
        public static CommentOnArticle of(Comment comment, UserResponse.UserDetail user) {
            List<CommentLike> commentLikes = comment.getLikes()
                    .stream()
                    .filter(like -> !like.isDeleted())
                    .toList();

            List<CommentDislike> commentDislikes = comment.getDislikes()
                    .stream()
                    .filter(dislike -> !dislike.isDeleted())
                    .toList();

            AtomicBoolean liked = new AtomicBoolean(false);
            commentLikes.stream().filter(like -> like.getCreatedBy().equals(user.getId())).findFirst().ifPresent(like -> {
                liked.set(true);
            });

            AtomicBoolean disliked = new AtomicBoolean(false);
            commentDislikes.stream().filter(dislike -> dislike.getCreatedBy().equals(user.getId())).findFirst().ifPresent(dislike -> {
                disliked.set(true);
            });

            return CommentOnArticle.builder()
                    .author(user)
                    .id(comment.getId())
                    .articleId(comment.getArticle().getId())
                    .content(comment.getContent())
                    .likeCount(commentLikes.size())
                    .dislikeCount(commentDislikes.size())
                    .liked(liked.get())
                    .disliked(disliked.get())
                    .createdBy(comment.getCreatedBy())
                    .updatedBy(comment.getUpdatedBy())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .build();
        }

    }
}
