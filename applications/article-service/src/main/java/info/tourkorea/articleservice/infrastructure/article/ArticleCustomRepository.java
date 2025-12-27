package info.tourkorea.articleservice.infrastructure.article;

import info.tourkorea.articleservice.domain.article.hashtag.Hashtag;
import info.tourkorea.articleservice.dto.article.ArticleRequest;
import info.tourkorea.articleservice.dto.article.ArticleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static info.tourkorea.articleservice.dto.article.ArticleDTO.*;

public interface ArticleCustomRepository {
    Page<ArticleQueryDTO> getArticles(ArticleRequest.ArticleSearchRequest request, Pageable pageable);

    Page<MyPageArticleQueryDTO> getMypageArticles(Long userId, String type, Pageable pageable);

    ArticleDetailQueryDTO getArticleDetail(Long id, Long userId);

    List<Hashtag> getHashtags(Long articleId);

    ArticleResponse.UserArticleCountResponse getUserArticleCounts(Long userId);
}
