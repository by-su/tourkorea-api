package info.tourkorea.articleservice.dto.article;

import info.tourkorea.articleservice.domain.article.hashtag.Hashtag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import request.UserContext;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ArticleRequest {

    @Getter
    public static class ArticleSearchRequest {
        String keyword;
        Boolean hashtagFlag;
        @Setter
        UserContext userContext;

        public ArticleSearchRequest(String keyword, Boolean hashtagFlag) {
            this.keyword = keyword;
            this.hashtagFlag = hashtagFlag == null ? false : hashtagFlag;
        }

    }

    @Getter
    @ToString
    public static class ArticleRegisterRequest {
        String title;
        String content;
        String hashtag;

        public static Set<Hashtag> convertHashtagStringtoList (String hashtagString) {
            if (hashtagString == null || hashtagString.isBlank()) {
                return Set.of();
            }

            return Arrays.stream(hashtagString.split("#"))
                    .skip(1)
                    .map(Hashtag::of)
                    .collect(Collectors.toSet());
        }
    }

    @Getter
    public static class ArticleUpdateRequest {
        String title;
        String content;
        String hashtag;
    }
}
