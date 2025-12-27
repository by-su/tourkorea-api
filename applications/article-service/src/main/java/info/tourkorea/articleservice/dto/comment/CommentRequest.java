package info.tourkorea.articleservice.dto.comment;

import lombok.Getter;

public class CommentRequest {

    @Getter
    public static class CommentCreateRequest {
        private String content;

        protected CommentCreateRequest() {}

        public CommentCreateRequest(String content) {
            this.content = content;
        }
    }

    @Getter
    public static class CommentUpdateRequest {
        private String content;

        protected CommentUpdateRequest() {}

        public CommentUpdateRequest(String content) {
            this.content = content;
        }
    }
}
