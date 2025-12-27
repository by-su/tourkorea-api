package info.tourkorea.articleservice.domain.article;


import info.tourkorea.articleservice.domain.BaseEntity;
import info.tourkorea.articleservice.domain.bookmark.Bookmark;
import info.tourkorea.articleservice.domain.comment.Comment;
import info.tourkorea.articleservice.domain.article.hashtag.Hashtag;
import info.tourkorea.articleservice.domain.article.like.Dislike;
import info.tourkorea.articleservice.domain.article.like.Liked;
import info.tourkorea.articleservice.dto.article.ArticleRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import request.UserContext;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static info.tourkorea.articleservice.dto.article.ArticleRequest.ArticleRegisterRequest.*;

@Entity
@Getter
//@ToString
public class Article extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500, nullable = false)
    private String title;
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;
    @OneToMany(mappedBy = "article")
    private List<Liked> likes;
    @OneToMany(mappedBy = "article")
    private List<Dislike> dislikes;
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Hashtag> hashtags;
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks;

    protected Article() {}

    @Builder
    public Article(String title, String content, List<Liked> likes, List<Dislike> dislikes, Set<Hashtag> hashtags, List<Comment> comments, List<Bookmark> bookmarks) {
        this.title = title;
        this.content = content;
        this.dislikes = dislikes == null ? List.of() : dislikes;
        this.likes = likes == null ? List.of() : likes;
        this.hashtags = hashtags == null ? Set.of() : hashtags;
        this.comments = comments == null ? List.of() : comments;
        this.bookmarks = bookmarks == null ? List.of() : bookmarks;
    }

    public static Article of(ArticleRequest.ArticleRegisterRequest request, UserContext userContext) {
        Set<Hashtag> hashtags = convertHashtagStringtoList(request.getHashtag());
        Article article = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .hashtags(hashtags)
                .build();
        hashtags.forEach(hashtag -> hashtag.setArticle(article));
        return article;
    }

    public void update(ArticleRequest.ArticleUpdateRequest request) {
        this.hashtags.clear();
        Set<Hashtag> hashtags = convertHashtagStringtoList(request.getHashtag());
        hashtags.forEach(hashtag -> hashtag.setArticle(this));
        this.hashtags.addAll(hashtags);
        this.title = request.getTitle();
        this.content = request.getContent();
    }

    public void deleteArticle() {
        this.setDeletedTrue();
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setArticle(this);
    }

    public void addHashtag(Hashtag hashtag) {
        this.hashtags.add(hashtag);
        hashtag.setArticle(this);
    }

    public Set<Long> getRelatedUserIds() {
        Set<Long> ids = this.comments.stream()
                .map(Comment::getCreatedBy)
                .collect(Collectors.toSet());

        ids.add(this.getCreatedBy());
        return ids;
    }
}
