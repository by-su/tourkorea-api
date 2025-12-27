package info.tourkorea.articleservice.dto.article;

import com.querydsl.core.annotations.QueryProjection;
import info.tourkorea.articleservice.domain.article.Article;
import info.tourkorea.articleservice.dto.user.UserResponse;
import info.tourkorea.componentsecurity.security.util.SecurityContextHolderUtil;
import info.tourkorea.exception.AuthorizationFailException;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import request.UserContext;
import response.ErrorCode;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static info.tourkorea.articleservice.dto.article.ArticleDTO.*;
import static info.tourkorea.articleservice.dto.article.HashtagResponse.HashtagInfo;
import static info.tourkorea.articleservice.dto.user.UserResponse.UserDetail;
import static info.tourkorea.articleservice.infrastructure.article.ArticleSanitizer.sanitize;

@Slf4j
public class ArticleResponse {

    @Builder
    public record ArticleGeneral(
            Long id,
            String title,
            String content,
            String author,
            Integer likeCount,
            Integer dislikeCount,
            Long createdBy,
            Long updatedBy,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt
    ) {


        public static ArticleGeneral of(Article article) {

            return ArticleGeneral.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .content(article.getContent())
                    .likeCount(article.getLikes().size())
                    .dislikeCount(article.getDislikes().size())
                    .createdBy(article.getCreatedBy())
                    .updatedBy(article.getUpdatedBy())
                    .createdAt(article.getCreatedAt())
                    .updatedAt(article.getUpdatedAt())
                    .build();

        }
    }

    public record ArticlesResponse(
            List<ArticleQueryAndUser> articles,
            response.Page page
    ) {
        public static ArticlesResponse of(Page<ArticleQueryDTO> articles, Set<UserDetail> users) {
            List<ArticleQueryDTO> content = articles.getContent();

            List<ArticleQueryAndUser> res = new ArrayList<>();
            for (ArticleQueryDTO article : content) {
                for (UserDetail user : users) {
                    if (article.getCreatedBy().equals(user.getId())) {
                        res.add(ArticleQueryAndUser.of(article, user));
                        break;
                    }
                }
            }

            return new ArticlesResponse(res, new response.Page(articles.getTotalPages(), articles.getTotalElements()));
        }
    }

    @Builder
    private record ArticleQueryAndUser(
            Long id,
            String title,
            String content,
            Boolean isOver300,
            Integer likeCount,
            Integer dislikeCount,
            Integer commentCount,
            Boolean liked,
            Boolean disliked,
            Long createdBy,
            Long updatedBy,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt,
            UserDetail author
    ) {
        public static ArticleQueryAndUser of(ArticleQueryDTO article, UserDetail user) {
            String content = article.getContent();
            String sanitize = sanitize(content);

            return ArticleQueryAndUser.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .content(sanitize)
                    .likeCount(article.getLikeCount())
                    .dislikeCount(article.getDislikeCount())
                    .commentCount(article.getCommentCount())
                    .liked(article.getLiked())
                    .disliked(article.getDisliked())
                    .createdBy(article.getCreatedBy())
                    .updatedBy(article.getUpdatedBy())
                    .createdAt(article.getCreatedAt())
                    .updatedAt(article.getUpdatedAt())
                    .author(user)
                    .build();
        }
    }

    @Builder
    public record ArticleDetailResponse(
            Long id,
            String title,
            String content,
            int likeCount,
            int dislikeCount,
            Boolean liked,
            Boolean disliked,
            Boolean bookmarked,
            Long createdBy,
            Long updatedBy,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt,
            UserDetail author,
            Set<String> hashtags,
            List<CommentDetailResponse> comments
    ) {}

    public record CommentDetailResponse(
            Long id,
            String content,
            int likeCount,
            int dislikeCount,
            boolean liked,
            boolean disliked,
            UserResponse.UserDetail author
    ) {

    }

//    @Builder
//    public record ArticleDetail (
//        Long id,
//        String title,
//        String content,
//        Integer likeCount,
//        Integer dislikeCount,
//        Boolean liked,
//        Boolean disliked,
//        Boolean bookmarked,
//        Long createdBy,
//        Long updatedBy,
//        ZonedDateTime createdAt,
//        ZonedDateTime updatedAt,
//        UserDetail author,
//        Set<String> hashtags,
//        List<CommentOnArticle> comments
//    ) {
//
//        public static ArticleDetail of(ArticleDTO.ArticleDetailQueryDTO article, List<Comment> comments, List<Hashtag> hashtags, Set<UserDetail> relatedUsers) {
//
//            // article author 찾기
//            UserDetail articleAuthor = null;
//            for (UserDetail user : relatedUsers) {
//                if (article.getCreatedBy().equals(user.getId())) {
//                    articleAuthor = user;
//                    break;
//                }
//            }
////
//            // CommentDetails 만들기
//            List<CommentOnArticle> commentResponse = new ArrayList<>();
//            for (Comment comment : comments) {
//                if (comment.isDeleted()) continue;
//                for (UserDetail user : relatedUsers) {
//                    if (comment.getCreatedBy().equals(user.getId())) {
//                        commentResponse.add(CommentOnArticle.of(comment, user));
//                    }
//                }
//            }
//
//            Optional<UserContext> loginUser = getUserContext();
//            Long loginUserId;
//            if (loginUser.isPresent()) {
//                loginUserId = loginUser.get().getId();
//            } else {
//                loginUserId = -1L;
//            }
//
//            return ArticleDetail.builder()
//                    .id(article.getId())
//                    .title(article.getTitle())
//                    .content(article.getContent())
//                    .likeCount(article.getLikeCount())
//                    .dislikeCount(article.getDislikeCount())
//                    .liked(article.getLiked())
//                    .disliked(article.getDisliked())
//                    .bookmarked(article.getBookmarked())
//                    .createdBy(article.getCreatedBy())
//                    .updatedBy(article.getUpdatedBy())
//                    .createdAt(article.getCreatedAt())
//                    .updatedAt(article.getUpdatedAt())
//                    .author(articleAuthor)
//                    .hashtags(
//                            hashtags.stream()
//                                    .map(Hashtag::getTag)
//                                    .collect(Collectors.toSet())
//                    )
//                    .comments(commentResponse)
//                    .build();
//        }
//    }

    @Builder
    public record ArticleCreatedResponse (
        Long id,
        String title,
        String content,
        Integer likeCount,
        Integer dislikeCount,
        Long createdBy,
        Long updatedBy,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt,
        List<HashtagInfo> hashtags
    ) {
        public static ArticleCreatedResponse of(Article article) {
            List<HashtagInfo> hashtags = article.getHashtags().stream()
                    .map(HashtagInfo::of)
                    .toList();
            return ArticleCreatedResponse.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .content(article.getContent())
                    .createdBy(article.getCreatedBy())
                    .updatedBy(article.getUpdatedBy())
                    .createdAt(article.getCreatedAt())
                    .updatedAt(article.getUpdatedAt())
                    .hashtags(hashtags)
                    .build();
        }
    }

    @Builder
    public record ArticleUpdateResponse (
        Long id,
        String title,
        String content,
        Long createdBy,
        Long updatedBy,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt,
        Set<HashtagInfo> hashtags
    ) {
        public static ArticleUpdateResponse of(Article article) {
            Set<HashtagInfo> hashtags = article.getHashtags().stream()
                    .map(HashtagInfo::of)
                    .collect(Collectors.toSet());

            return ArticleUpdateResponse.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .content(article.getContent())
                    .createdBy(article.getCreatedBy())
                    .updatedBy(article.getUpdatedBy())
                    .createdAt(article.getCreatedAt())
                    .updatedAt(article.getUpdatedAt())
                    .hashtags(hashtags)
                    .build();
        }
    }

    @Builder
    public record MyPageArticleResponse(
            List<MyPageArticleAndUser> articles,
            UserProfile userProfile,
            response.Page page
    ) {
        public static MyPageArticleResponse of(Page<MyPageArticleQueryDTO> articles, Set<UserDetail> users, UserArticleCountResponse myArticleCount) {
            List<MyPageArticleQueryDTO> content = articles.getContent();

            List<MyPageArticleAndUser> res = new ArrayList<>();
            for (MyPageArticleQueryDTO article : content) {
                for (UserDetail user : users) {
                    if (article.getCreatedBy().equals(user.getId())) {
                        res.add(MyPageArticleAndUser.of(article, user));
                        break;
                    }
                }
            }

            UserContext userContext = SecurityContextHolderUtil.getUserContext().orElseThrow(() -> new AuthorizationFailException(ErrorCode.AUTHORIZATION_FAIL));
            UserDetail loginUser = null;
            for (UserDetail userDetail : users) {
                if (userDetail.getId().equals(userContext.getId())) {
                    loginUser = userDetail;
                }
            }

            return new MyPageArticleResponse(
                    res,
                    new UserProfile(loginUser, myArticleCount.getArticleCount(), myArticleCount.getBookmarkCount()),
                    new response.Page(articles.getTotalPages(), articles.getTotalElements()));
        }
    }

    @Builder
    private record MyPageArticleAndUser(
            UserDetail author,
            Long id,
            String title,
            String content,
            Integer likeCount,
            Integer dislikeCount,
            Integer commentCount,
            Boolean liked,
            Boolean disliked,
            Long articleCount,
            Long bookmarkCount,
            Long createdBy,
            Long updatedBy,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt
    ) {
        public static MyPageArticleAndUser of(MyPageArticleQueryDTO article, UserDetail user) {
            return MyPageArticleAndUser.builder()
                    .author(user)
                    .id(article.getId())
                    .title(article.getTitle())
                    .content(article.getContent())
                    .likeCount(article.getLikeCount())
                    .dislikeCount(article.getDislikeCount())
                    .commentCount(article.getCommentCount())
                    .liked(article.getLiked())
                    .disliked(article.getDisliked())
                    .articleCount(article.getArticleCount())
                    .bookmarkCount(article.getBookmarkCount())
                    .createdBy(article.getCreatedBy())
                    .updatedBy(article.getUpdatedBy())
                    .createdAt(article.getCreatedAt())
                    .updatedAt(article.getUpdatedAt())
                    .build();
        }
    }

    private record UserProfile(
            UserDetail user,
            Long articleCount,
            Long bookmarkCount
    ) {

    }

    @Data
    @NoArgsConstructor
    public static class MyPageArticleDTO {
        private Long id;
        private String title;
        private String content;
        private String author;
        private Integer likeCount;
        private Integer dislikeCount;
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
        public MyPageArticleDTO(Long id, String title, String content, String author, int likeCount, int dislikeCount, Boolean liked, Boolean disliked, Long articleCount, Long bookmarkCount, Long createdBy, Long updatedBy, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.author = author;
            this.likeCount = likeCount;
            this.dislikeCount = dislikeCount;
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

    @Getter
    public static class UserArticleCountResponse {
        private final Long articleCount;
        private final Long bookmarkCount;

        @Builder
        public UserArticleCountResponse(Long articleCount, Long bookmarkCount) {
            this.articleCount = articleCount;
            this.bookmarkCount = bookmarkCount;
        }
    }
}
