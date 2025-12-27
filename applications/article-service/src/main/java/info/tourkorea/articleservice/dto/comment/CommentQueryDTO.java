package info.tourkorea.articleservice.dto.comment;

import com.querydsl.core.annotations.QueryProjection;

import java.time.ZonedDateTime;

public class CommentQueryDTO {

    public static class CommentDetailQueryDTO {
        private final Long id;
        private final Long articleId;
        private final String content;
        private final Long createdBy;
        private final Long updatedBy;
        private final ZonedDateTime createdAt;
        private final ZonedDateTime updatedAt;

        @QueryProjection
        public CommentDetailQueryDTO(Long id, Long articleId, String content, Long createdBy, Long updatedBy, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
            this.id = id;
            this.articleId = articleId;
            this.content = content;
            this.createdBy = createdBy;
            this.updatedBy = updatedBy;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }

    public static class CommentLikeQueryDTO {
        private final Long id;
        private final Long userId;
        private final Long createdBy;
        private final Long updatedBy;
        private final ZonedDateTime createdAt;
        private final ZonedDateTime updatedAt;

        @QueryProjection
        public CommentLikeQueryDTO(Long id, Long userId, Long createdBy, Long updatedBy, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
            this.id = id;
            this.userId = userId;
            this.createdBy = createdBy;
            this.updatedBy = updatedBy;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }

    public static class CommentDislikeQueryDTO {
        private final Long id;
        private final Long userId;
        private final Long createdBy;
        private final Long updatedBy;
        private final ZonedDateTime createdAt;
        private final ZonedDateTime updatedAt;

        @QueryProjection
        public CommentDislikeQueryDTO(Long id, Long userId, Long createdBy, Long updatedBy, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
            this.id = id;
            this.userId = userId;
            this.createdBy = createdBy;
            this.updatedBy = updatedBy;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }
}
