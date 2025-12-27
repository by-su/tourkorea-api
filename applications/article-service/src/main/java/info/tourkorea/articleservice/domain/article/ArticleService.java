package info.tourkorea.articleservice.domain.article;

import info.tourkorea.articleservice.domain.BaseEntity;
import info.tourkorea.articleservice.domain.article.hashtag.Hashtag;
import info.tourkorea.articleservice.domain.bookmark.Bookmark;
import info.tourkorea.articleservice.domain.bookmark.BookmarkRepository;
import info.tourkorea.articleservice.domain.article.like.Dislike;
import info.tourkorea.articleservice.domain.article.like.DislikeRepository;
import info.tourkorea.articleservice.domain.article.like.LikeRepository;
import info.tourkorea.articleservice.domain.article.like.Liked;
import info.tourkorea.articleservice.dto.article.HashtagResponse;
import info.tourkorea.articleservice.dto.article.like.LikeResponse;
import info.tourkorea.exception.AuthenticationFailException;
import info.tourkorea.exception.AuthorizationFailException;
import info.tourkorea.exception.DuplicatedEntityException;
import info.tourkorea.exception.NotFoundEntityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import request.UserContext;
import response.ErrorCode;

import java.util.List;
import java.util.Set;

import static info.tourkorea.articleservice.dto.article.ArticleDTO.*;
import static info.tourkorea.articleservice.dto.article.ArticleRequest.*;
import static info.tourkorea.articleservice.dto.article.ArticleResponse.*;
import static info.tourkorea.componentsecurity.security.util.SecurityContextHolderUtil.getUserContext;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final BookmarkRepository bookmarkRepository;
    private final LikeRepository likeRepository;
    private final DislikeRepository dislikeRepository;
    private static final Long UNREGISTERED = -1L;

    public Article getArticle(Long id) {

        return articleRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new NotFoundEntityException(ErrorCode.NOT_FOUND_ARTICLE.getErrorMsg()));
    }

    public Page<ArticleQueryDTO> getArticles(ArticleSearchRequest request, Pageable pageable) {
        UserContext userContext = getUserContext().isEmpty() ? null : getUserContext().get();
        request.setUserContext(userContext);

        return articleRepository.getArticles(request, pageable);
    }

    public ArticleCreatedResponse createArticle(ArticleRegisterRequest request) {
        UserContext userContext = getUserContext().orElseThrow(() -> new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL));
        Article article = Article.of(request, userContext);
        articleRepository.save(article);
        return ArticleCreatedResponse.of(article);
    }

    public ArticleUpdateResponse updateArticle(Long id, ArticleUpdateRequest request) {
        Article article = articleRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new NotFoundEntityException(ErrorCode.ENTITY_NOT_FOUND.getErrorMsg()));
        article.update(request);
        articleRepository.save(article);
        return ArticleUpdateResponse.of(article);
    }

    public void deleteArticle(Long id) {
        UserContext userContext = getUserContext().orElseThrow(() -> new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL));
        Article article = articleRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new NotFoundEntityException(ErrorCode.ENTITY_NOT_FOUND.getErrorMsg()));

        if (!article.getCreatedBy().equals(userContext.getId())) throw new AuthorizationFailException(ErrorCode.AUTHORIZATION_FAIL);
        article.deleteArticle();
    }

    public void saveBookmark(Long id) {
        UserContext userContext = getUserContext().orElseThrow(() -> new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL));
        Article article = articleRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new NotFoundEntityException(ErrorCode.ENTITY_NOT_FOUND.getErrorMsg()));

        var _bookmark = bookmarkRepository.findByUserIdAndArticle(userContext.getId(), article);
        if (_bookmark.isEmpty()) {
            Bookmark bookmark = new Bookmark(userContext.getId(), article);
            bookmarkRepository.save(bookmark);
            return;
        }

        Bookmark bookmark = _bookmark.get();
        if (bookmark.isDeleted()) bookmark.setDeletedFalse();
        else throw new DuplicatedEntityException(ErrorCode.DUPLICATED_BOOKMARK.getErrorMsg());
    }

    public void deleteBookmark(Long id) {
        UserContext userContext = getUserContext().orElseThrow(() -> new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL));
        Article article = articleRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new NotFoundEntityException(ErrorCode.ENTITY_NOT_FOUND.getErrorMsg()));

        Bookmark bookmark = bookmarkRepository.findByUserIdAndArticleAndDeletedFalse(userContext.getId(), article).orElseThrow(() -> new NotFoundEntityException(ErrorCode.ENTITY_NOT_FOUND.getErrorMsg()));
        bookmark.setDeletedTrue();
    }


    public Page<MyPageArticleQueryDTO> getUserArticles(UserContext userContext, String type, Pageable pageable) {
        return articleRepository.getMypageArticles(userContext.getId(), type, pageable);
    }

    public List<Hashtag> getHashtags(Long articleId) {
        return articleRepository.getHashtags(articleId);
    }



    public LikeResponse.LikedNewYnDTO liked(Long id) {
        var userContext = getUserContext().orElseThrow(() -> new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL));
        var article = articleRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new NotFoundEntityException(ErrorCode.ENTITY_NOT_FOUND.getErrorMsg()));

        var optionalLiked = likeRepository.findByUserIdAndArticle(userContext.getId(), article);

        // 싫어요 눌린 상태면 싫어요 취소
        dislikeRepository.findByUserIdAndArticleAndDeletedFalse(userContext.getId(), article).ifPresent(BaseEntity::setDeletedTrue);

        if (optionalLiked.isEmpty()) {
            Liked liked = new Liked(userContext.getId(), article);
            likeRepository.save(liked);
            // newYn 처음 좋아요를 누른 경우 true (알림 전송을 위함)
            return LikeResponse.LikedNewYnDTO.of(liked, true, true);
        }

        // 좋아요 눌린 상태면 취소, 취소 상태면 좋아요
        Liked liked = optionalLiked.get();
        if (liked.isDeleted()) liked.setDeletedFalse();
        else liked.setDeletedTrue();

        // 좋아요 눌린건지 취소된 건지 여부 반환
        // deleted가 true면 취소된 상태, false면 좋아요 눌린 상태
        return LikeResponse.LikedNewYnDTO.of(liked, !liked.isDeleted(), false);
    }

    public boolean disliked(Long id) {
        var userContext = getUserContext().orElseThrow(() -> new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL));
        var article = articleRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new NotFoundEntityException(ErrorCode.ENTITY_NOT_FOUND.getErrorMsg()));

        // 싫어요 눌린 상태면 싫어요 취소
        likeRepository.findByUserIdAndArticleAndDeletedFalse(userContext.getId(), article).ifPresent(BaseEntity::setDeletedTrue);

        var _dislike = dislikeRepository.findByUserIdAndArticle(userContext.getId(), article);
        if (_dislike.isEmpty()) {
            Dislike dislike = new Dislike(userContext.getId(), article);
            dislikeRepository.save(dislike);

            return true;
        }

        // 싫어요 눌린 상태면 취소, 취소 상태면 싫어요
        Dislike disliked = _dislike.get();
        if (disliked.isDeleted()) disliked.setDeletedFalse();
        else disliked.setDeletedTrue();

        // 싫어요 눌린건지 취소된 건지 여부 반환
        // deleted가 true면 취소된 상태, false면 좋아요 눌린 상태
        return !disliked.isDeleted();
    }

    public UserArticleCountResponse getMyArticleCount(UserContext userContext) {
        if (userContext == null) throw new AuthorizationFailException(ErrorCode.AUTHENTICATION_FAIL);

        return articleRepository.getUserArticleCounts(userContext.getId());
    }
}
