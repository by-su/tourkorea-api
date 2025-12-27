package info.tourkorea.articleservice.application;

import info.tourkorea.articleservice.domain.article.ArticleService;
import info.tourkorea.articleservice.domain.article.hashtag.Hashtag;
import info.tourkorea.articleservice.domain.comment.Comment;
import info.tourkorea.articleservice.domain.comment.CommentService;
import info.tourkorea.articleservice.dto.article.ArticleDTO;
import info.tourkorea.articleservice.dto.article.ArticleRequest;
import info.tourkorea.articleservice.dto.article.like.LikeResponse;
import info.tourkorea.articleservice.infrastructure.notification.NotificationCaller;
import info.tourkorea.articleservice.infrastructure.user.UserCaller;
import info.tourkorea.articleservice.infrastructure.user.UserFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import request.UserContext;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static info.tourkorea.articleservice.dto.article.ArticleResponse.*;
import static info.tourkorea.articleservice.dto.article.like.LikeResponse.LikedNewYnDTO;
import static info.tourkorea.articleservice.dto.user.UserResponse.UserDetail;
import static info.tourkorea.componentsecurity.security.util.SecurityContextHolderUtil.getUserContext;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleFacade {

    private final ArticleService articleService;
    private final CommentService commentService;
    private final UserCaller userCaller;
    private final UserFeignClient userFeignClient;
    private final NotificationCaller notificationCaller;
    private static final Long UNREGISTERED = -1L;

    // Todo: 리팩토링 필요
    public ArticleDetailResponse getArticleDetail(Long id) {
        final var article = articleService.getArticle(id);

        UserContext userContext = getUserContext().orElse(null);
        log.info("userContext = {}", userContext);
        Long loginId = userContext == null ? UNREGISTERED : userContext.getId();

        log.info("Likes: {}", article.getLikes());
        log.info("Dislikes: {}", article.getDislikes());

        boolean liked = article.getLikes().stream().anyMatch(like -> like.getUserId().equals(loginId));
        boolean disliked = article.getDislikes().stream().anyMatch(dislike -> dislike.getUserId().equals(loginId));
        boolean bookmarked = article.getBookmarks().stream().anyMatch(bookmark -> bookmark.getUserId().equals(loginId));

        List<Comment> comments = article.getComments();
        log.info("comments = {}", comments);

        Set<Long> userIds = comments.stream().map(Comment::getCreatedBy).collect(Collectors.toSet());
        userIds.add(article.getCreatedBy());
        Set<UserDetail> users = userFeignClient.getUsers(userIds);

        List<CommentDetailResponse> commentDetails = comments.stream()
                .map(comment -> new CommentDetailResponse(
                        comment.getId(),
                        comment.getContent(),
                        comment.getLikes().size(),
                        comment.getDislikes().size(),
                        comment.getLikes().stream().anyMatch(like -> like.getUserId().equals(loginId)),
                        comment.getDislikes().stream().anyMatch(dislike -> dislike.getUserId().equals(loginId)),
                        users.stream().filter(u -> u.getId().equals(comment.getCreatedBy())).findFirst().orElse(null)
                ))
                .toList();

        Set<String> hashtags = article.getHashtags().stream().map(Hashtag::getTag).collect(Collectors.toSet());

        UserDetail articleAuthor = users.stream()
                .filter(u -> u.getId().equals(article.getCreatedBy()))
                .findFirst()
                .orElse(UserDetail.builder().username("unknown").build());

        log.info("articleAuthor = {}", articleAuthor);

        return ArticleDetailResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .likeCount(article.getLikes().size())
                .dislikeCount(article.getDislikes().size())
                .liked(liked)
                .disliked(disliked)
                .bookmarked(bookmarked)
                .createdBy(article.getCreatedBy())
                .updatedBy(article.getUpdatedBy())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .author(articleAuthor)
                .hashtags(hashtags)
                .comments(commentDetails)
                .build();
    }

    private static Set<Long> extractUserIdFromCommentAndArticle(List<Comment> comments, Long articleAuthorId) {
        Set<Long> ids = comments.stream()
                .map(Comment::getCreatedBy)
                .collect(Collectors.toSet());
        ids.add(articleAuthorId);
        return ids;
    }

    public ArticlesResponse getArticles(ArticleRequest.ArticleSearchRequest request, Pageable pageable) {
        final var articles = articleService.getArticles(request, pageable);
        final var users = userCaller.getUserSetFromArticleQuery(articles.getContent());
        return ArticlesResponse.of(articles, users);
    }

    @Transactional
    public ArticleCreatedResponse createArticle(ArticleRequest.ArticleRegisterRequest registerRequest) {
        return articleService.createArticle(registerRequest);
    }

    @Transactional
    public ArticleUpdateResponse updateArticle(Long id, ArticleRequest.ArticleUpdateRequest request) {

        return articleService.updateArticle(id, request);
    }

    @Transactional
    public void deleteArticle(Long id) {
        articleService.deleteArticle(id);
    }

    @Transactional
    public void saveBookmark(Long id) {

        articleService.saveBookmark(id);
    }

    @Transactional
    public void deleteBookmark(Long id) {

        articleService.deleteBookmark(id);
    }


    public MyPageArticleResponse getUserArticles(UserContext userContext, String type, PageRequest of) {
        Page<ArticleDTO.MyPageArticleQueryDTO> articles = articleService.getUserArticles(userContext, type, of);
        UserArticleCountResponse myArticleCount = articleService.getMyArticleCount(userContext);
        Set<UserDetail> users = userCaller.getUserSet(articles.getContent());

        return MyPageArticleResponse.of(articles, users, myArticleCount);
    }


    @Transactional
    public boolean liked(Long articleId) {
        LikedNewYnDTO likedNewYnDTO = articleService.liked(articleId);
        // 알림 발송 로직
        notificationCaller.articleLikeNotification(likedNewYnDTO);
        return likedNewYnDTO.likedYn();
    }

    @Transactional
    public boolean disliked(Long articleId) {
        return articleService.disliked(articleId);
    }
}
