package info.tourkorea.articleservice.domain.comment;

import info.tourkorea.articleservice.domain.BaseEntity;
import info.tourkorea.articleservice.domain.article.Article;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString(exclude = "comment")
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_USER_ID_COMMENT_ID",
                        columnNames = {"user_id", "comment_id"}
                )
        }
)
public class CommentDislike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    protected CommentDislike() {}

    public CommentDislike(Long userId, Comment comment) {
        this.userId = userId;
        this.comment = comment;
    }
}
