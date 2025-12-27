package info.tourkorea.articleservice.domain.article.like;

import info.tourkorea.articleservice.domain.article.Article;
import info.tourkorea.articleservice.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_USER_ID_ARTICLE_ID",
                        columnNames = {"user_id", "article_id"}
                )
        }
)
public class Liked extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    protected Liked() {}

    public Liked(Long userId, Article article) {
        this.userId = userId;
        this.article = article;
    }
}
