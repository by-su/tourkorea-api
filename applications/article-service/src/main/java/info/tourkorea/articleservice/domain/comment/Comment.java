package info.tourkorea.articleservice.domain.comment;

import info.tourkorea.articleservice.domain.article.Article;
import info.tourkorea.articleservice.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// 대댓글 없고, 멘션으로 구현
@Entity
@Getter
@ToString(exclude = "article")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500, nullable = false)
    private String content;
    @Column(length = 50, nullable = false)
    private String author;

    @OneToMany(mappedBy = "comment")
    private final List<CommentLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "comment")
    private final List<CommentDislike> dislikes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Builder
    public Comment(String content, String author, Article article) {
        this.content = content;
        this.author = author;
        this.article = article;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
