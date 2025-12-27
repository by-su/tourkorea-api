package info.tourkorea.articleservice.dto.notification;

import info.tourkorea.articleservice.dto.article.ArticleResponse;
import info.tourkorea.articleservice.dto.comment.CommentResponse;
import info.tourkorea.exception.AuthenticationFailException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import response.ErrorCode;

import static info.tourkorea.articleservice.dto.comment.CommentResponse.*;
import static info.tourkorea.componentsecurity.security.util.SecurityContextHolderUtil.getUserContext;

public class NotificationRequest {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequest {
        private Long userId;
        private Long senderId;
        private Long contentId;
        private String type;
        private String comment;

        public static RegisterRequest of(Long userId, Long senderId, Long contentId, String type, String comment) {
            return RegisterRequest.builder()
                    .userId(userId)
                    .senderId(senderId)
                    .contentId(contentId)
                    .type(type)
                    .comment(comment)
                    .build();
        }

        public static RegisterRequest likeOnPost(Long senderId, ArticleResponse.ArticleGeneral article) {
            return NotificationRequest.RegisterRequest.builder()
                    .userId(article.createdBy())
                    .senderId(senderId)
                    .contentId(article.id())
                    .type("LIKE_ON_POST")
                    .build();
        }

        public static RegisterRequest likeOnComment(Long senderId, CommentGeneral commentGeneral) {
            return RegisterRequest.builder()
                    .userId(commentGeneral.createdBy())
                    .senderId(senderId)
                    .contentId(commentGeneral.id())
                    .type("LIKE_ON_COMMENT")
                    .build();
        }

        public static RegisterRequest commentOnPost(Long senderId, CommentGeneral commentGeneral) {
            return RegisterRequest.builder()
                    .userId(commentGeneral.createdBy())
                    .senderId(senderId)
                    .contentId(commentGeneral.id())
                    .comment(commentGeneral.content())
                    .type("COMMENT_ON_POST")
                    .build();
        }
    }


}
