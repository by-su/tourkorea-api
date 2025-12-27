package info.tourkorea.articleservice.dto.article.like;

import info.tourkorea.articleservice.domain.article.like.Liked;
import info.tourkorea.articleservice.dto.article.ArticleResponse;

public class LikeResponse {

    public record LikeDTO(
            Long id,
            Long userId,
            ArticleResponse.ArticleGeneral article
    ) {
        public static LikeDTO of(Liked liked) {
            return new LikeDTO(
                    liked.getId(),
                    liked.getUserId(),
                    ArticleResponse.ArticleGeneral.of(liked.getArticle())
            );
        }
    }
    
    public record LikedNewYnDTO(
            LikeDTO liked,
            boolean likedYn,
            boolean newYn
    ) {
        public static LikedNewYnDTO of(Liked liked, boolean likedYn,boolean newYn) {
            return new LikedNewYnDTO(LikeDTO.of(liked), likedYn, newYn);
        }
    }


}
