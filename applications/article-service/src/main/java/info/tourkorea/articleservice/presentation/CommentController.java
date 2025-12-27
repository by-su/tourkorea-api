package info.tourkorea.articleservice.presentation;

import info.tourkorea.articleservice.application.CommentFacade;
import info.tourkorea.articleservice.dto.comment.like.CommentLikeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import response.ApiResponse;

import static info.tourkorea.articleservice.dto.comment.CommentRequest.*;
import static info.tourkorea.articleservice.dto.comment.CommentResponse.*;

@Slf4j
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentFacade commentFacade;

    @PostMapping("/{articleId}")
    public ApiResponse<CommentGeneral> createComment(@PathVariable Long articleId, @RequestBody CommentCreateRequest request) {
        var response = commentFacade.saveComment(articleId, request);
        return ApiResponse.success(HttpStatus.CREATED.name(), response);
    }

    // Todo : 페이징
    @GetMapping
    public ApiResponse<?> getMyComments(@PageableDefault(size = 5, page = 0) Pageable pageable) {
        var response = commentFacade.getMyComments(pageable);
        return ApiResponse.success(HttpStatus.OK.name(), response);
    }

    @PatchMapping("/{commentId}")
    public ApiResponse<CommentGeneral> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest request) {
        CommentGeneral commentGeneral = commentFacade.updateComment(commentId, request);
        return ApiResponse.success(HttpStatus.OK.name(), commentGeneral);
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long commentId) {
        commentFacade.deleteComment(commentId);
        return ApiResponse.success(HttpStatus.OK.name(), null, "댓글이 삭제되었습니다.");
    }

    @PostMapping("/{commentId}/likes")
    public ApiResponse<Boolean> likeComment(@PathVariable Long commentId) {

        return ApiResponse.success(HttpStatus.OK.name(), commentFacade.likeComment(commentId), "댓글 좋아요가 처리되었습니다.");
    }

    @PostMapping("/{commentId}/dislikes")
    public ApiResponse<Boolean> dislikeComment(@PathVariable Long commentId) {
        return ApiResponse.success(HttpStatus.OK.name(), commentFacade.dislikeComment(commentId), "댓글 싫어요가 처리되었습니다.");
    }

}
