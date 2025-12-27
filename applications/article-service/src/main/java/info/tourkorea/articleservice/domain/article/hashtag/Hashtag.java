package info.tourkorea.articleservice.domain.article.hashtag;

import info.tourkorea.articleservice.domain.article.Article;
import info.tourkorea.articleservice.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString(exclude = "article")
public class Hashtag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    protected Hashtag() {}

    public static Hashtag of(String tag) {
        Hashtag hashtag = new Hashtag();
        hashtag.tag = tag;
        return hashtag;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
