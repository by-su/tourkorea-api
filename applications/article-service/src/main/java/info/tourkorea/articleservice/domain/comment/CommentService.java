package info.tourkorea.articleservice.domain.comment;

import info.tourkorea.articleservice.domain.article.Article;
import info.tourkorea.articleservice.domain.article.ArticleRepository;
import info.tourkorea.componentsecurity.security.util.SecurityContextHolderUtil;
import info.tourkorea.exception.AuthenticationFailException;
import info.tourkorea.exception.DuplicatedEntityException;
import info.tourkorea.exception.NotFoundEntityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import request.UserContext;
import response.ErrorCode;

import java.util.List;
import java.util.Optional;

import static info.tourkorea.articleservice.dto.comment.CommentRequest.CommentCreateRequest;
import static info.tourkorea.articleservice.dto.comment.CommentRequest.CommentUpdateRequest;
import static info.tourkorea.articleservice.dto.comment.CommentResponse.CommentGeneral;
import static info.tourkorea.articleservice.dto.comment.CommentResponse.MyPageComments;
import static info.tourkorea.articleservice.dto.comment.like.CommentLikeResponse.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentDislikeRepository commentDislikeRepository;
    private final ArticleRepository articleRepository;

    public MyPageComments getMyComments(Pageable pageable) {
        UserContext user = SecurityContextHolderUtil.getUserContext().orElseThrow(() -> new AuthenticationFailException(ErrorCode.AUTHORIZATION_FAIL));
        Page<Comment> _comments = commentRepository.findByCreatedByAndDeletedFalse(user.getId(), pageable);

        List<CommentGeneral> comments = _comments.getContent()
                .stream()
                .map(comment -> CommentGeneral.of(comment, user))
                .toList();


        return MyPageComments.of(comments, _comments.getTotalPages());
    }

    public CommentGeneral saveComment(Long articleId, CommentCreateRequest request) {
        UserContext user = SecurityContextHolderUtil.getUserContext().orElseThrow(() -> new AuthenticationFailException(ErrorCode.AUTHORIZATION_FAIL));
        Article article = articleRepository.findByIdAndDeletedFalse(articleId).orElseThrow(() -> new NotFoundEntityException(ErrorCode.NOT_FOUND_ARTICLE.getErrorMsg()));

        Comment comment = Comment.builder()
                .article(article)
                .content(request.getContent())
                .author(user.getUsername())
                .build();
        commentRepository.save(comment);

        return CommentGeneral.of(comment, user);
    }

    public CommentGeneral updateComment(Long commentId, CommentUpdateRequest request) {
        UserContext user = SecurityContextHolderUtil.getUserContext().orElseThrow(() -> new AuthenticationFailException(ErrorCode.AUTHORIZATION_FAIL));

        Comment comment = commentRepository.findByIdAndDeletedFalse(commentId).orElseThrow(() -> new NotFoundEntityException(ErrorCode.NOT_FOUND_COMMENT.getErrorMsg()));
        if (!user.getId().equals(comment.getCreatedBy()))
            throw new AuthenticationFailException(ErrorCode.AUTHORIZATION_FAIL);

        comment.updateContent(request.getContent());

        return CommentGeneral.of(comment, user);
    }

    public void deleteComment(Long CommentId) {
        Comment comment = commentRepository.findByIdAndDeletedFalse(CommentId).orElseThrow(() -> new NotFoundEntityException(ErrorCode.NOT_FOUND_COMMENT.getErrorMsg()));
        comment.setDeletedTrue();
    }


    public CommentLikeNewYnDTO likeComment(Long commentId) {
        UserContext userContext = SecurityContextHolderUtil.getUserContext().orElseThrow(() -> new AuthenticationFailException(ErrorCode.AUTHORIZATION_FAIL));
        Comment comment = commentRepository.findByIdAndDeletedFalse(commentId).orElseThrow(() -> new NotFoundEntityException(ErrorCode.NOT_FOUND_COMMENT.getErrorMsg()));

        // 싫어요 눌린 상태면 좋아요 취소
        commentDislikeRepository.findByUserIdAndCommentAndDeletedFalse(userContext.getId(), comment).ifPresent(CommentDislike::setDeletedTrue);

        Optional<CommentLike> _commentLike = commentLikeRepository.findByUserIdAndComment(userContext.getId(), comment);
        if (_commentLike.isEmpty()) {
            CommentLike commentLike = new CommentLike(userContext.getId(), comment);
            commentLikeRepository.save(commentLike);
            // newYn 처음 좋아요를 누른 경우 true (알림 전송을 위함)
            return CommentLikeNewYnDTO.of(commentLike, true, true);
        }

        CommentLike commentLike = _commentLike.get();
        if (commentLike.isDeleted()) commentLike.setDeletedFalse();
        else commentLike.setDeletedTrue();

        return CommentLikeNewYnDTO.of(commentLike, !commentLike.getDeleted(),  false);
    }

    public boolean dislikeComment(Long commentId) {
        UserContext userContext = SecurityContextHolderUtil.getUserContext().orElseThrow(() -> new AuthenticationFailException(ErrorCode.AUTHORIZATION_FAIL));
        Comment comment = commentRepository.findByIdAndDeletedFalse(commentId).orElseThrow(() -> new NotFoundEntityException(ErrorCode.NOT_FOUND_COMMENT.getErrorMsg()));

        Optional<CommentDislike> _commentDislike = commentDislikeRepository.findByUserIdAndComment(userContext.getId(), comment);

        // 좋아요 눌린 상태면 좋아요 취소
        commentLikeRepository.findByUserIdAndCommentAndDeletedFalse(userContext.getId(), comment).ifPresent(CommentLike::setDeletedTrue);

        if (_commentDislike.isEmpty()) {
            CommentDislike commentDislike = new CommentDislike(userContext.getId(), comment);
            commentDislikeRepository.save(commentDislike);
            return true;
        }

        CommentDislike commentDislike = _commentDislike.get();
        if (commentDislike.isDeleted()) commentDislike.setDeletedFalse();
        else commentDislike.setDeletedTrue();

        return !commentDislike.getDeleted();
    }

    public List<Comment> getCommentsByArticleId(Long id) {
        return commentRepository.findByArticleIdAndDeletedFalse(id);
    }
}
