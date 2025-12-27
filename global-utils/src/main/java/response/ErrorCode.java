package response;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 모니터링 대상 에러
    SERVER_ERROR("INTERNAL_SERVER_ERROR", "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
    COMMON_FILE_UPLOAD_FAIL("INTERNAL_SERVER_ERROR", "File upload fail"),

    // 비즈니스 로직 에러 (모니터링 대상 아님)
    DUPLICATED_VALUE("사용할 수 없는 값 입니다."),
    DUPLICATED_EMAIL("이미 존재하는 이메일입니다."),
    DUPLICATED_USERNAME("이미 존재하는 사용자 이름입니다."),
    DUPLICATED_BOOKMARK("You've already bookmarked this article."),
    DUPLICATED_ENTITY("이미 존재하는 엔티티입니다."),
    DUPLICATED_LIKE("You've already liked this article."),
    DUPLICATED_DISLIKE("You've already disliked this article."),
    DUPLICATED_COMMENT_LIKE("You've already liked this comment."),
    DUPLICATED_COMMENT_DISLIKE("You've already disliked this comment."),

    FILE_SIZE_OVER("File size is too large"),

    INVALID_PARAMETER("요청한 값이 올바르지 않습니다."),

    ENTITY_NOT_FOUND("존재하지 않는 엔티티입니다."),
    NOT_FOUND_ARTICLE("존재하지 않는 게시글입니다."),
    NOT_FOUND_COMMENT("존재하지 않는 댓글입니다."),
    NOT_FOUND_COMMENT_LIKE("존재하지 않는 댓글 좋아요입니다."),
    NOT_FOUND_COMMENT_DISLIKE("존재하지 않는 댓글 싫어요입니다."),

    AUTHENTICATION_FAIL("인증 정보가 올바르지 않습니다."),
    NOT_GOOGLE_LOGIN("The user is not registered with Google."),
    AUTHORIZATION_FAIL("권한이 없습니다."),
    INCORRECT_USER("사용자 정보가 올바르지 않습니다."),
    FILE_NOT_FOUND("존재하지 않는 파일입니다."),

    DATA_LENGTH_OVER_EXCEPTION("데이터 길이가 초과되었습니다."),

    // 토큰 에러,
    JWT_NOTFOUND("토큰이 존재하지 않습니다."),
    PASSWORD_NOT_MATCH("패스워드가 일치하지 않습니다.");


    private final String status;
    private final String errorMsg;

    ErrorCode(String errorMsg) {
        this.status = "BAD_REQUEST";
        this.errorMsg = errorMsg;
    }

    ErrorCode(String status, String errorMsg) {
        this.status = status;
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg(Object... arg) {
        return String.format(errorMsg, arg);
    }
}
