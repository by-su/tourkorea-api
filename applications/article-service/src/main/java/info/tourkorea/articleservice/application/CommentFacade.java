package info.tourkorea.articleservice.application;

import info.tourkorea.articleservice.domain.comment.CommentService;
import info.tourkorea.articleservice.dto.comment.CommentRequest;
import info.tourkorea.articleservice.dto.comment.like.CommentLikeResponse.CommentLikeNewYnDTO;
import info.tourkorea.articleservice.infrastructure.notification.NotificationCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static info.tourkorea.articleservice.dto.comment.CommentResponse.CommentGeneral;
import static info.tourkorea.articleservice.dto.comment.CommentResponse.MyPageComments;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentFacade {

    private final CommentService commentService;
    private final NotificationCaller notificationCaller;

    public MyPageComments getMyComments(Pageable pageable) {
        return commentService.getMyComments(pageable);
    }

    @Transactional
    public CommentGeneral saveComment(Long articleId, CommentRequest.CommentCreateRequest request) {
        CommentGeneral commentGeneral = commentService.saveComment(articleId, request);
        // 댓글은 달릴 때마다 알림을 생성
        notificationCaller.saveCommentNotification(commentGeneral);
        return commentGeneral;
    }

    @Transactional
    public CommentGeneral updateComment(Long id, CommentRequest.CommentUpdateRequest request) {
        return commentService.updateComment(id, request);
    }

    @Transactional
    public void deleteComment(Long id) {
        commentService.deleteComment(id);
    }


    @Transactional
    public boolean likeComment(Long id) {
        CommentLikeNewYnDTO commentLikeNewYnDTO = commentService.likeComment(id);
        // 알림 발송 로직
        notificationCaller.commentLikeNotification(commentLikeNewYnDTO);

        return commentLikeNewYnDTO.likedYn();
    }

    @Transactional
    public boolean dislikeComment(Long id) {
        return commentService.dislikeComment(id);
    }

}
