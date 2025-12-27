package info.tourkorea.articleservice.dto.hashtag;

import com.querydsl.core.annotations.QueryProjection;

import java.time.ZonedDateTime;

public class HashtagQueryDTO {

    public static class HashtagDetailQueryDTO {

        private final Long id;
        private final String tag;
        private final Long articleId;
        private final Long createdBy;
        private final Long updatedBy;
        private final ZonedDateTime createdAt;
        private final ZonedDateTime updatedAt;

        @QueryProjection
        public HashtagDetailQueryDTO(Long id, String tag, Long articleId, Long createdBy, Long updatedBy, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
            this.id = id;
            this.tag = tag;
            this.articleId = articleId;
            this.createdBy = createdBy;
            this.updatedBy = updatedBy;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }
}
