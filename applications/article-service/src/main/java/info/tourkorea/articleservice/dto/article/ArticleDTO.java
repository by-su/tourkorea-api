package info.tourkorea.articleservice.dto.article;

import com.querydsl.core.annotations.QueryProjection;
import info.tourkorea.articleservice.domain.article.Article;
import info.tourkorea.articleservice.domain.article.hashtag.Hashtag;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static info.tourkorea.articleservice.dto.comment.CommentQueryDTO.*;
import static info.tourkorea.articleservice.dto.hashtag.HashtagQueryDTO.*;

public class ArticleDTO {

    @Getter
    public static class ArticleQueryDTO {
        private Long id;
        private String title;
        private String content;
        private Integer likeCount;
        private Integer dislikeCount;
        private Integer commentCount;
        private Boolean liked;
        private Boolean disliked;
        private Long createdBy;
        private Long updatedBy;
        private ZonedDateTime createdAt;
        private ZonedDateTime updatedAt;

        @Builder
        @QueryProjection
        public ArticleQueryDTO(Long id, String title, String content, Integer likeCount, Integer dislikeCount, Integer commentCount, Boolean liked, Boolean disliked, Long createdBy, Long updatedBy, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.likeCount = likeCount;
            this.dislikeCount = dislikeCount;
            this.commentCount = commentCount;
            this.liked = liked;
            this.disliked = disliked;
            this.createdBy = createdBy;
            this.updatedBy = updatedBy;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public static ArticleQueryDTO of(Article article) {
            return ArticleQueryDTO.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .content(article.getContent())
                    .likeCount(article.getLikes().size())
                    .dislikeCount(article.getDislikes().size())
                    .commentCount(article.getComments().size())
                    .createdBy(article.getCreatedBy())
                    .updatedBy(article.getUpdatedBy())
                    .createdAt(article.getCreatedAt())
                    .updatedAt(article.getUpdatedAt())
                    .build();
        }
    }

    @Getter
    public static class ArticleDetailQueryDTO {
        private final Long id;
        private final String title;
        private final String content;
        private final Integer likeCount;
        private final Integer dislikeCount;
        private final Boolean liked;
        private final Boolean disliked;
        private final Boolean bookmarked;
        private final Long createdBy;
        private final Long updatedBy;
        private final ZonedDateTime createdAt;
        private final ZonedDateTime updatedAt;

        @QueryProjection
        public ArticleDetailQueryDTO(Long id, String title, String content, Integer likeCount, Integer dislikeCount, Boolean liked, Boolean disliked, Boolean bookmarked, Long createdBy, Long updatedBy, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.likeCount = likeCount;
            this.dislikeCount = dislikeCount;
            this.liked = liked;
            this.disliked = disliked;
            this.bookmarked = bookmarked;
            this.createdBy = createdBy;
            this.updatedBy = updatedBy;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }

    @Getter
    public static class MyPageArticleQueryDTO {
        private Long id;
        private String title;
        private String content;
        private Integer likeCount;
        private Integer dislikeCount;
        private Integer commentCount;
        private Boolean liked;
        private Boolean disliked;
        private Long articleCount;
        private Long bookmarkCount;
        private Long createdBy;
        private Long updatedBy;
        private ZonedDateTime createdAt;
        private ZonedDateTime updatedAt;

        @Builder
        @QueryProjection
        public MyPageArticleQueryDTO(Long id, String title, String content, Integer likeCount, Integer dislikeCount, Integer commentCount, Boolean liked, Boolean disliked, Long articleCount, Long bookmarkCount, Long createdBy, Long updatedBy, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.likeCount = likeCount;
            this.dislikeCount = dislikeCount;
            this.commentCount = commentCount;
            this.liked = liked;
            this.disliked = disliked;
            this.articleCount = articleCount;
            this.bookmarkCount = bookmarkCount;
            this.createdBy = createdBy;
            this.updatedBy = updatedBy;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }
}