package info.tourkorea.articleservice.presentation;

import info.tourkorea.articleservice.application.ArticleFacade;
import info.tourkorea.articleservice.dto.article.like.LikeResponse;
import info.tourkorea.articleservice.dto.article.like.LikeResponse.LikedNewYnDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import request.UserContext;
import response.ApiResponse;

import static info.tourkorea.articleservice.dto.article.ArticleRequest.*;
import static info.tourkorea.articleservice.dto.article.ArticleResponse.*;


@Slf4j
@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
@Tag(name = "게시판 API", description = "게시판 및 게시글 API")
public class ArticleController {

    private final ArticleFacade articleFacade;

    @GetMapping("/{id}")
    public ApiResponse<ArticleDetailResponse> getArticleDetail(@PathVariable Long id) {
        var response = articleFacade.getArticleDetail(id);
        return ApiResponse.success(HttpStatus.OK.name(), response);
    }

    @GetMapping
    public ApiResponse<?> getArticles(
            @ModelAttribute ArticleSearchRequest request,
            @PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        var response = articleFacade.getArticles(request, pageable);
        return ApiResponse.success(HttpStatus.OK.name(), response);
    }

    @PostMapping
    public ApiResponse<ArticleCreatedResponse> createArticle(
            @RequestBody ArticleRegisterRequest registerRequest
    ) {
        log.info("RegisterRequest = {}", registerRequest);
        var response = articleFacade.createArticle(registerRequest);
        return ApiResponse.success(HttpStatus.CREATED.name(), response);
    }


    @PatchMapping("/{id}")
    public ApiResponse<ArticleUpdateResponse> updateArticle(@PathVariable Long id, @RequestBody ArticleUpdateRequest request) {
        var response = articleFacade.updateArticle(id, request);
        return ApiResponse.success(HttpStatus.OK.name(), response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteArticle(@PathVariable Long id) {
        articleFacade.deleteArticle(id);
        return ApiResponse.success(HttpStatus.OK.name(), null, "The article has been deleted.");
    }

    @PostMapping("/{id}/bookmarks")
    public ApiResponse<String> saveBookmark(@PathVariable Long id) {
        articleFacade.saveBookmark(id);
        return ApiResponse.success(HttpStatus.OK.name(), null, "The post has been bookmarked.");
    }

    @DeleteMapping("/{id}/bookmarks")
    public ApiResponse<String> deleteBookmark(@PathVariable Long id) {
        articleFacade.deleteBookmark(id);
        return ApiResponse.success(HttpStatus.OK.name(), null, "The post has been unbookmarked.");
    }

    @PostMapping("/{id}/likes")
    public ApiResponse<Boolean> saveLike(@PathVariable Long id) {
        return ApiResponse.success(HttpStatus.OK.name(), articleFacade.liked(id), "The article has been liked.");
    }


    @PostMapping("/{id}/dislikes")
    public ApiResponse<Boolean> saveDislike(@PathVariable Long id) {
        return ApiResponse.success(HttpStatus.OK.name(), articleFacade.disliked(id), "The article has been disliked.");
    }


    @GetMapping("/mypage")
    public MyPageArticleResponse getProfileContents(
            @RequestParam String type,
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "5", required = false) Integer size,
            @AuthenticationPrincipal UserContext userContext
    ) {
        return articleFacade.getUserArticles(userContext, type, PageRequest.of(page, size));
    }

}
