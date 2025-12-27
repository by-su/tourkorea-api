package info.tourkorea.articleservice.dto.article;

import java.time.ZonedDateTime;

public class HashtagResponse {

    public record HashtagInfo(
            Long id,
            String tag,
            Long createdBy,
            Long updatedBy,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt
    ) {
        public static HashtagInfo of(info.tourkorea.articleservice.domain.article.hashtag.Hashtag hashtag) {
            return new HashtagInfo(
                    hashtag.getId(),
                    hashtag.getTag(),
                    hashtag.getCreatedBy(),
                    hashtag.getUpdatedBy(),
                    hashtag.getCreatedAt(),
                    hashtag.getUpdatedAt()
            );
        }
    }
}
