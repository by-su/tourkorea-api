package info.tourkorea.articleservice.infrastructure.notification;

import info.tourkorea.articleservice.dto.article.ArticleResponse;
import info.tourkorea.articleservice.dto.article.like.LikeResponse.LikedNewYnDTO;
import info.tourkorea.articleservice.dto.comment.like.CommentLikeResponse;
import info.tourkorea.articleservice.dto.notification.NotificationRequest;
import info.tourkorea.exception.AuthenticationFailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import request.UserContext;
import response.ErrorCode;

import static info.tourkorea.articleservice.dto.comment.CommentResponse.*;
import static info.tourkorea.componentsecurity.security.util.SecurityContextHolderUtil.getUserContext;

@Component
@RequiredArgsConstructor
public class NotificationCaller {

    private final NotificationFeignClient notificationFeignClient;
    // 게시글 좋아요 알림
    // 새로 생성된 좋아요 알림만 전송
    public void articleLikeNotification(LikedNewYnDTO likedNewYnDTO) {
        var userContext = getUserContext().orElseThrow(() -> new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL));
        if (isMyArticle(userContext, likedNewYnDTO.liked().article())) { return; }
        if (!likedNewYnDTO.newYn()) { return; }
        var registerRequest = NotificationRequest.RegisterRequest.likeOnPost(userContext.getId(), likedNewYnDTO.liked().article());
        notificationFeignClient.createNotification(registerRequest);
    }

    // 댓글 좋아요 알림
    // 새로 생성된 좋아요 알림만 전송
    public void commentLikeNotification(CommentLikeResponse.CommentLikeNewYnDTO commentNewYnDTO) {
        var userContext = getUserContext().orElseThrow(() -> new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL));
        if (isMyComment(userContext, commentNewYnDTO.commentLikeDTO().comment())) { return; }
        if (!commentNewYnDTO.newYn()) { return; }
        var registerRequest = NotificationRequest.RegisterRequest.likeOnComment(userContext.getId(), commentNewYnDTO.commentLikeDTO().comment());
        notificationFeignClient.createNotification(registerRequest);
    }

    // 댓글 알림
    public void saveCommentNotification(CommentGeneral commentGeneral) {
        var userContext = getUserContext().orElseThrow(() -> new AuthenticationFailException(ErrorCode.AUTHENTICATION_FAIL));
        if (isMyComment(userContext, commentGeneral)) { return; }
        var registerRequest = NotificationRequest.RegisterRequest.commentOnPost(userContext.getId(), commentGeneral);
        notificationFeignClient.createNotification(registerRequest);
    }

    // 내 게시글인지 확인
    private boolean isMyArticle(UserContext userContext, ArticleResponse.ArticleGeneral articleGeneral) {
        return articleGeneral.createdBy().equals(userContext.getId());
    }

    // 내 댓글인지 확인
    private boolean isMyComment(UserContext userContext, CommentGeneral commentGeneral) {
        return commentGeneral.createdBy().equals(userContext.getId());
    }

}
